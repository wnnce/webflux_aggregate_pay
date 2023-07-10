package com.zeroxn.pay.module.alipay.config;

import com.alipay.api.AlipayClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 下午1:38
 * @Description: 开启支付宝支付的启动注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnClass(AlipayClient.class)
@Import(AlipayPayAutoConfiguration.class)
public @interface EnableAlipayPay {
}
