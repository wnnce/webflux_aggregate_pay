package com.zeroxn.pay.core.amqp.kafka.config;

import com.zeroxn.pay.core.amqp.PayMQTemplate;
import com.zeroxn.pay.core.amqp.kafka.PayMQKafkaTemplate;
import com.zeroxn.pay.core.amqp.kafka.PayMQKafkaTopicManager;
import com.zeroxn.pay.core.amqp.kafka.runner.PayMQKafkaRunner;
import com.zeroxn.pay.core.register.ModuleRegistry;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @Author: lisang
 * @DateTime: 2023/5/22 下午9:57
 * @Description:
 */
@AutoConfiguration
@ConditionalOnProperty(value = "pay.mq.kafka.enable", havingValue = "true")
@ConditionalOnClass(KafkaTemplate.class)
@EnableConfigurationProperties(PayMQKafkaProperties.class)
@ConditionalOnMissingBean(PayMQTemplate.class)
public class PayMQKafkaAutoConfiguration {
    private final PayMQKafkaProperties properties;
    public PayMQKafkaAutoConfiguration(PayMQKafkaProperties properties){
        this.properties = properties;
    }

    @Bean
    public PayMQKafkaTopicManager payMQKafkaTopicManager(){
        return new PayMQKafkaTopicManager();
    }
    @Bean
    @ConditionalOnClass(PayMQKafkaTopicManager.class)
    public PayMQKafkaRunner payMQKafkaRunner(ModuleRegistry moduleRegistry, AdminClient adminClient,
                                             PayMQKafkaTopicManager topicManager){
        return new PayMQKafkaRunner(properties.getTopicName(), moduleRegistry, adminClient, topicManager);
    }
    @Bean
    @ConditionalOnClass(PayMQKafkaTopicManager.class)
    public PayMQTemplate payMQKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate, PayMQKafkaTopicManager topicManager){
        return new PayMQKafkaTemplate(kafkaTemplate, properties.getTopicName(), topicManager);
    }
}
