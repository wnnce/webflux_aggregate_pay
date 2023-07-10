package com.zeroxn.pay.module.paypal.config;

import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 下午3:11
 * @Description: 开启Paypal支付模块的启动注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableScheduling
@Import(PaypalAutoConfiguration.class)
public @interface EnablePaypalPay {
}
