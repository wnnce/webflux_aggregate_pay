package com.zeroxn.pay.core.amqp.rabbit.config;

import com.zeroxn.pay.core.amqp.PayMQTemplate;
import com.zeroxn.pay.core.amqp.rabbit.PayMQRabbitQueueManager;
import com.zeroxn.pay.core.amqp.rabbit.PayMQRabbitTemplate;
import com.zeroxn.pay.core.amqp.rabbit.runner.PayMQRabbitRunner;
import com.zeroxn.pay.core.config.ModuleRegistry;
import com.zeroxn.pay.core.config.PayAutoConfiguration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午5:31
 * @Description: RabbitMQ自动配置交换机和消息队列
 */
@Configuration
@AutoConfigureAfter(PayAutoConfiguration.class)
@ConditionalOnProperty(value = "pay.mq.rabbitmq.enable", havingValue = "true")
@ConditionalOnClass(RabbitTemplate.class)
@EnableConfigurationProperties(PayMQRabbitProperties.class)
@ConditionalOnMissingBean(PayMQTemplate.class)
public class PayMQRabbitAutoConfiguration {
    private final PayMQRabbitProperties properties;
    public PayMQRabbitAutoConfiguration(PayMQRabbitProperties properties){
        this.properties = properties;
    }
    @Bean
    public DirectExchange payDirectExchange(){
        return new DirectExchange(properties.getExchangeName());
    }
    @Bean
    @ConditionalOnClass(PayMQRabbitProperties.class)
    public PayMQRabbitQueueManager payMQRabbitQueueManager(){
        return new PayMQRabbitQueueManager(properties.getQueueName(), properties.getQueueKey());
    }
    @Bean
    @ConditionalOnClass(PayMQRabbitQueueManager.class)
    public PayMQRabbitRunner payMQRabbitRunner(ModuleRegistry moduleRegistry, AmqpAdmin amqpAdmin,
                                               PayMQRabbitQueueManager queueManager){
        return new PayMQRabbitRunner(moduleRegistry, properties.getExchangeName(), amqpAdmin, queueManager);
    }
    @Bean
    @ConditionalOnClass(value = {PayMQRabbitQueueManager.class, PayMQRabbitRunner.class})
    public PayMQTemplate payMQRabbitTemplate(RabbitTemplate rabbitTemplate, PayMQRabbitProperties properties,
                                             PayMQRabbitQueueManager queueManager){
        return new PayMQRabbitTemplate(rabbitTemplate, properties.getExchangeName(), queueManager);
    }
}
