package com.zeroxn.pay.module.jdpay.config;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 14:59:38
 * @Description:
 */
public class ConditionalOnJdPay implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Object application = beanFactory.getBean("payApplication");
        return application.getClass().isAnnotationPresent(EnableJdPay.class);
    }
}
