package com.zeroxn.pay.module.jdpay.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 14:58:38
 * @Description: 开启京东支付的注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(JdPayAutoConfiguration.class)
public @interface EnableJdPay {
}
