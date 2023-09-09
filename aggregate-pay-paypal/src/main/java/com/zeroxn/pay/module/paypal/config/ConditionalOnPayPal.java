package com.zeroxn.pay.module.paypal.config;


import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 14:11:46
 * @Description: 自定义条件注入注解，通过判断启动类上是否使用了开启某个支付模块的注解来控制注入支付模块的Controller和Service
 */
public class ConditionalOnPayPal implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Object application = beanFactory.getBean("payApplication");
        return application.getClass().isAnnotationPresent(EnablePaypalPay.class);
    }
}