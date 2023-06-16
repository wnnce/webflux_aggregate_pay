package com.zeroxn.pay.core.mq.rabbit.config;

import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.mq.PayMQTemplate;
import com.zeroxn.pay.core.mq.rabbit.PayMQRabbitTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午5:31
 * @Description: RabbitMQ自动配置交换机和消息队列
 */
@Configuration
@AutoConfigureBefore(PayMQRabbitAutoConfiguration.class)
@ConditionalOnProperty(value = "pay.mq.rabbitmq.enable", havingValue = "true")
@ConditionalOnClass(RabbitTemplate.class)
@EnableConfigurationProperties(PayMQRabbitProperties.class)
@ConditionalOnMissingBean(PayMQTemplate.class)
public class PayMQRabbitAutoConfiguration {
    private final PayMQRabbitProperties properties;
    public PayMQRabbitAutoConfiguration(PayMQRabbitProperties properties){
        this.properties = properties;
    }
    @Bean
    public DirectExchange payDirectExchange(){
        return new DirectExchange(properties.getExchangeName());
    }

    @Bean
    @ConditionalOnProperty(value = "pay.rabbitmq.enable-jackson", havingValue = "true")
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    @ConditionalOnProperty(value = "pay.wechat.enable", havingValue = "true")
    public Queue wechatSuccessQueue(){
        return new Queue(properties.getSuccessQueueName(PayPlatform.WECHAT));
    }
    @Bean
    @ConditionalOnProperty(value = "pay.wechat.enable", havingValue = "true")
    public Queue wechatRefundQueue(){
        return new Queue(properties.getRefundQueueName(PayPlatform.WECHAT));
    }
    @Bean
    @ConditionalOnProperty(value = "pay.alipay.enable", havingValue = "true")
    public Queue alipaySuccessQueue(){
        return new Queue(properties.getSuccessQueueName(PayPlatform.ALIPAY));
    }
    @Bean
    @ConditionalOnProperty(value = "pay.union.enable", havingValue = "true")
    public Queue unionSuccessQueue(){
        return new Queue(properties.getSuccessQueueName(PayPlatform.UNION));
    }
    @Bean
    @ConditionalOnProperty(value = "pay.union.enable", havingValue = "true")
    public Queue unionRefundQueue(){
        return new Queue(properties.getRefundQueueName(PayPlatform.UNION));
    }
    @Bean
    @ConditionalOnBean(name = {"payDirectExchange", "wechatSuccessQueue"})
    public Binding directBindingWechatSuccessQueue(Queue wechatSuccessQueue, DirectExchange payDirectExchange){
        return BindingBuilder
                .bind(wechatSuccessQueue)
                .to(payDirectExchange)
                .with(properties.getSuccessQueueKey(PayPlatform.WECHAT));
    }
    @Bean
    @ConditionalOnBean(name = {"payDirectExchange", "wechatRefundQueue"})
    public Binding directBindingWechatRefundQueue(Queue wechatRefundQueue, DirectExchange payDirectExchange){
        return BindingBuilder
                .bind(wechatRefundQueue)
                .to(payDirectExchange)
                .with(properties.getRefundQueueKey(PayPlatform.WECHAT));
    }
    @Bean
    @ConditionalOnBean(name = {"payDirectExchange", "alipaySuccessQueue"})
    public Binding directBindingAlipaySuccessQueue(Queue alipaySuccessQueue, DirectExchange payDirectExchange){
        return BindingBuilder
                .bind(alipaySuccessQueue)
                .to(payDirectExchange)
                .with(properties.getSuccessQueueKey(PayPlatform.ALIPAY));
    }
    @Bean
    @ConditionalOnBean(name = {"payDirectExchange", "unionSuccessQueue"})
    public Binding directBindingUnionSuccessQueue(Queue unionSuccessQueue, DirectExchange payDirectExchange){
        return BindingBuilder
                .bind(unionSuccessQueue)
                .to(payDirectExchange)
                .with(properties.getSuccessQueueKey(PayPlatform.UNION));
    }
    @Bean
    @ConditionalOnBean(name = {"payDirectExchange", "unionRefundQueue"})
    public Binding directBindingUnionRefundQueue(Queue unionRefundQueue, DirectExchange payDirectExchange){
        return BindingBuilder
                .bind(unionRefundQueue)
                .to(payDirectExchange)
                .with(properties.getRefundQueueKey(PayPlatform.UNION));
    }
    @Bean
    public PayMQTemplate payMQRabbitTemplate(RabbitTemplate rabbitTemplate, PayMQRabbitProperties properties){
        return new PayMQRabbitTemplate(rabbitTemplate, properties);
    }
}
