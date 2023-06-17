package com.zeroxn.pay.core.mq.kafka.config;

import com.zeroxn.pay.core.mq.PayMQTemplate;
import com.zeroxn.pay.core.mq.kafka.PayMQKafkaTemplate;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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

    //TODO 待实现：通过获取PayTemplate接口的实现类 来实现自动声明所有的Topic分区
    @Bean
    public NewTopic payTopic(){
        return TopicBuilder
                .name(properties.getTopicName())
                .partitions(5)
                .replicas(1)
                .build();
    }
    @Bean
    public PayMQTemplate payMQKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate, PayMQKafkaProperties properties){
        return new PayMQKafkaTemplate(kafkaTemplate, properties);
    }
}
