package com.zeroxn.pay.core.mq.rabbit;

import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;
import com.zeroxn.pay.core.interfaces.PayMQTemplate;
import com.zeroxn.pay.core.mq.config.PayMQRabbitProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午5:24
 * @Description:
 */
public class PayMQRabbitTemplate implements PayMQTemplate {
    private final RabbitTemplate rabbitTemplate;
    private final PayMQRabbitProperties properties;

    public PayMQRabbitTemplate(RabbitTemplate rabbitTemplate, PayMQRabbitProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    @Override
    public void send(PayPlatform platform, PayResult result, Object data) {
        String[] nameAndKey = getNameAndKey(platform, result);
        System.out.println(Arrays.toString(nameAndKey));
        rabbitTemplate.convertAndSend(nameAndKey[0], nameAndKey[1], data);
    }

    @Override
    public String[] getNameAndKey(PayPlatform platform, PayResult result) {
        String exchangeName = properties.getExchangeName();
        String queueKey;
        if (platform == PayPlatform.WECHAT){
            if (result == PayResult.SUCCESS){
                queueKey = properties.getWechatSuccessQueueKey();
            }else {
                queueKey = properties.getWechatRefundQueueKey();
            }
        }else {
            queueKey = properties.getAlipaySuccessQueueKey();
        }
        return new String[]{exchangeName, queueKey};
    }
}
