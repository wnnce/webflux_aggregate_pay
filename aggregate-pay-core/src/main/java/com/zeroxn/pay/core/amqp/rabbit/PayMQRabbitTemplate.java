package com.zeroxn.pay.core.amqp.rabbit;

import com.zeroxn.pay.core.amqp.PayMQTemplate;
import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午5:24
 * @Description: PayMQTemplate的RabbitMQ实现类
 */
public class PayMQRabbitTemplate implements PayMQTemplate {
    private static final Logger logger = LoggerFactory.getLogger(PayMQRabbitTemplate.class);
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final PayMQRabbitQueueManager queueManager;

    public PayMQRabbitTemplate(RabbitTemplate rabbitTemplate, String exchangeName ,
                               PayMQRabbitQueueManager queueManager) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.queueManager = queueManager;
    }

    @Override
    public void send(PayPlatform platform, PayResult result, String data) {
        String queueKey = getQueueKey(platform, result);
        rabbitTemplate.convertAndSend(exchangeName, queueKey, data);
        PayMQRabbitTemplate.logger.info("向RabbitMQ发送消息，路由名：{}，KEY：{}", exchangeName, queueKey);
    }

    /**
     * 通过传入的支付平台和结果来获取需要路由到的Queue的key
     * @param platform 支付平台
     * @param result 结果，支付成功 or 退款成功
     * @return 返回路由需要的KEY
     */
    private String getQueueKey(PayPlatform platform, PayResult result) {
        return queueManager.getQueueKey(platform, result);
    }
}
