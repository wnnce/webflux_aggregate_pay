package com.zeroxn.pay.core.mq.kafka;

import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;
import com.zeroxn.pay.core.mq.PayMQTemplate;
import com.zeroxn.pay.core.mq.kafka.config.PayMQKafkaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午5:24
 * @Description: PayMQTemplate的kafka实现类
 */
public class PayMQKafkaTemplate implements PayMQTemplate {
    private static final Logger logger = LoggerFactory.getLogger(PayMQKafkaTemplate.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PayMQKafkaProperties properties;
    public PayMQKafkaTemplate(KafkaTemplate<String, Object> kafkaTemplate, PayMQKafkaProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
    }

    @Override
    public void send(PayPlatform platform, PayResult result, Object data) {
        int partition = getPartition(platform, result);
        String topicName = properties.getTopicName();
        String uuid = UUID.randomUUID().toString();
        kafkaTemplate.send(topicName, partition, uuid, data).thenAccept(success -> {
            logger.info("Kafka消息发送成功，Topic：{}，Partition：{}，KEY：{}", topicName, partition, uuid);
        }).exceptionally(error -> {
            logger.error("kafka消息发送失败，错误消息：{}", error.getMessage());
            return null;
        });
    }

    /**
     * 通过传入的支付平台和结果来获取Kafka Topic的分区
     * @param platform 支付平台
     * @param result 结果
     * @return Topic的Partition
     */
    private int getPartition(PayPlatform platform, PayResult result){
        if(platform == PayPlatform.WECHAT){
            if (result == PayResult.SUCCESS){
                return 1;
            }else {
                return 2;
            }
        }else {
            return 3;
        }
    }
}
