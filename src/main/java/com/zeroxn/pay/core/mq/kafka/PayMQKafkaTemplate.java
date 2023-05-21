package com.zeroxn.pay.core.mq.kafka;

import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;
import com.zeroxn.pay.core.interfaces.PayMQTemplate;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午5:24
 * @Description:
 */
public class PayMQKafkaTemplate implements PayMQTemplate {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public PayMQKafkaTemplate(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(PayPlatform platform, PayResult result, Object data) {

    }

    @Override
    public String[] getNameAndKey(PayPlatform platform, PayResult result) {
        return new String[0];
    }
}
