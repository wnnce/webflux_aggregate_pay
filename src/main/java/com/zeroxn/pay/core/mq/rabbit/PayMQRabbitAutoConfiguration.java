package com.zeroxn.pay.core.mq.rabbit;

import com.zeroxn.pay.core.interfaces.PayMQTemplate;
import com.zeroxn.pay.core.mq.config.PayMQRabbitProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午5:31
 * @Description: RabbitMQ自动配置交换机和消息队列
 */
@Configuration
@AutoConfigureBefore(PayMQRabbitAutoConfiguration.class)
@ConditionalOnProperty(value = "pay.rabbitmq.enable", havingValue = "true")
@ConditionalOnClass(RabbitTemplate.class)
@EnableConfigurationProperties(PayMQRabbitProperties.class)
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
    @ConditionalOnProperty(value = "pay.wechat.enable", havingValue = "true")
    public Queue wechatSuccessQueue(){
        return new Queue(properties.getWechatRefundQueueName());
    }
    @Bean
    @ConditionalOnProperty(value = "pay.wechat.enable", havingValue = "true")
    public Queue wechatRefundQueue(){
        return new Queue(properties.getWechatSuccessQueueName());
    }
    @Bean
    @ConditionalOnProperty(value = "pay.alipay.enable", havingValue = "true")
    public Queue alipaySuccessQueue(){
        return new Queue(properties.getAlipaySuccessQueueName());
    }
    @Bean
    @ConditionalOnBean(name = {"payDirectExchange", "wechatSuccessQueue"})
    public Binding directBindingWechatSuccessQueue(Queue wechatSuccessQueue, DirectExchange directExchange){
        return BindingBuilder.bind(wechatSuccessQueue).to(directExchange).with(properties.getWechatSuccessQueueKey());
    }
    @Bean
    @ConditionalOnBean(name = {"payDirectExchange", "wechatRefundQueue"})
    public Binding directBindingWechatRefundQueue(Queue wechatRefundQueue, DirectExchange directExchange){
        return BindingBuilder.bind(wechatRefundQueue).to(directExchange).with(properties.getWechatRefundQueueKey());
    }
    @Bean
    @ConditionalOnBean(name = {"payDirectExchange", "alipaySuccessQueue"})
    public Binding directBindingAlipaySuccessQueue(Queue alipaySuccessQueue, DirectExchange directExchange){
        return BindingBuilder.bind(alipaySuccessQueue).to(directExchange).with(properties.getAlipaySuccessQueueKey());
    }
    @Bean
    @ConditionalOnMissingBean(PayMQTemplate.class)
    public PayMQTemplate payMQTemplate(RabbitTemplate rabbitTemplate, PayMQRabbitProperties properties){
        return new PayMQRabbitTemplate(rabbitTemplate, properties);
    }
}
