package com.zeroxn.pay.module.union.config;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 14:23:19
 * @Description:
 */
public class ConditionalOnUnion implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Object application = beanFactory.getBean("payApplication");
        return application.getClass().isAnnotationPresent(EnableUnionPay.class);
    }
}
