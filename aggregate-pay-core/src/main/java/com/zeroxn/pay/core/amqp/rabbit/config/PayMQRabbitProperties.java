package com.zeroxn.pay.core.amqp.rabbit.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午7:26
 * @Description:
 */
@ConfigurationProperties(prefix = "pay.mq.rabbitmq")
@ConditionalOnProperty(value = "pay.mq.rabbitmq.enable", havingValue = "true")
@ConditionalOnClass(RabbitTemplate.class)
public class PayMQRabbitProperties {
    /**
     * 是否开启RabbitMQ消息队列 需要先引入依赖
     */
    private final Boolean enable;
    private final Boolean enableJackson;
    /**
     * DirectExchange交换机名称
     */
    private final String exchangeName;
    /**
     * 支付成功推送消息的Queue名称，程序处理后的命名规则为 queueName + {支付平台} + {success/refund}，比如：xxx.alipay.success
     */
    private final String queueName;
    /**
     * 支付成功推送消息的Queue的KEY，程序处理后的命名规则为 queueKey + {支付平台} + {success/refund}，比如：xxx-queue-refund
     */
    private final String queueKey;
    @ConstructorBinding
    public PayMQRabbitProperties(Boolean enable,@DefaultValue("false") Boolean enableJackson, @DefaultValue("zeroxn.pay") String exchangeName,
                                 @DefaultValue("pay") String queueName, @DefaultValue("key") String queueKey){
        this.enable = enable;
        this.enableJackson = enableJackson;
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.queueKey = queueKey;

    }

    public boolean getEnable() {
        return enable;
    }
    public Boolean getEnableJackson() {
        return enableJackson;
    }
    public String getExchangeName() {
        return exchangeName;
    }
    public String getQueueName() {
        return queueName;
    }

    public String getQueueKey() {
        return queueKey;
    }
}
