package com.zeroxn.pay.core.mq.kafka.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午7:26
 * @Description:
 */
@ConfigurationProperties(prefix = "pay.kafka")
@ConditionalOnProperty(value = "pay.kafka.enable", havingValue = "true")
@ConditionalOnClass(KafkaTemplate.class)
public class PayMQKafkaProperties {
    private final Boolean enable;
    private final String topicName;
    @ConstructorBinding
    public PayMQKafkaProperties(Boolean enable, @DefaultValue("zeroxn.pay.topic") String topicName){
        this.enable = enable;
        this.topicName = topicName;
    }

    public boolean getEnable() {
        return enable;
    }

    public String getTopicName() {
        return topicName;
    }
}
