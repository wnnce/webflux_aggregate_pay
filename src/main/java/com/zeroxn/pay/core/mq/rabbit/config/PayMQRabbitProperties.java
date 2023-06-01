package com.zeroxn.pay.core.mq.rabbit.config;

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
     * 支付成功推送消息的Queue名称，程序处理后的命名规则为 successQueueName + [.{支付平台}]，比如：success.alipay
     */
    private final String successQueueName;
    /**
     * 支付成功推送消息的Queue的KEY，程序处理后的命名规则为 successQueueName + [-{支付平台}]，比如：success-alipay
     */
    private final String successQueueKey;
    /**
     * 退款成功推送消息的Queue名称，命名规则同
     */
    private final String refundQueueName;
    /**
     * 退款成功推送消息的Queue的KEY
     */
    private final String refundQueueKey;
    @ConstructorBinding
    public PayMQRabbitProperties(Boolean enable,@DefaultValue("false") Boolean enableJackson, @DefaultValue("zeroxn.pay") String exchangeName,
                                 @DefaultValue("success") String successQueueName, @DefaultValue("su") String successQueueKey,
                                 @DefaultValue("refund") String refundQueueName, @DefaultValue("re") String refundQueueKey){
        this.enable = enable;
        this.enableJackson = enableJackson;
        this.exchangeName = exchangeName;
        this.successQueueName = successQueueName;
        this.successQueueKey = successQueueKey;
        this.refundQueueName = refundQueueName;
        this.refundQueueKey = refundQueueKey;
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
    public String getWechatSuccessQueueName(){
        return this.successQueueName + ".wechat";
    }
    public String getWechatSuccessQueueKey(){
        return this.successQueueKey + "-wechat";
    }
    public String getAlipaySuccessQueueName(){
        return this.successQueueName + ".alipay";
    }
    public String getAlipaySuccessQueueKey(){
        return this.successQueueKey + "-alipay";
    }
    public String getWechatRefundQueueName(){
        return this.refundQueueName + ".wechat";
    }
    public String getWechatRefundQueueKey(){
        return this.refundQueueKey + "-wechat";
    }
}
