package com.zeroxn.pay.core.amqp.kafka.config;

import com.zeroxn.pay.core.amqp.PayMQTemplate;
import com.zeroxn.pay.core.amqp.kafka.PayMQKafkaTemplate;
import com.zeroxn.pay.core.amqp.kafka.PayMQKafkaTopicManager;
import com.zeroxn.pay.core.amqp.kafka.runner.PayMQKafkaRunner;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @Author: lisang
 * @DateTime: 2023/5/22 下午9:57
 * @Description:
 */
@Configuration
@AutoConfigureBefore(PayMQKafkaAutoConfiguration.class)
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
    public PayMQKafkaRunner payMQKafkaRunner(ApplicationContext context, AdminClient adminClient,
                                             PayMQKafkaTopicManager topicManager){
        return new PayMQKafkaRunner(properties.getTopicName(), context, adminClient, topicManager);
    }
    @Bean
    @ConditionalOnClass(PayMQKafkaTopicManager.class)
    public PayMQTemplate payMQKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate, PayMQKafkaTopicManager topicManager){
        return new PayMQKafkaTemplate(kafkaTemplate, properties.getTopicName(), topicManager);
    }
}
