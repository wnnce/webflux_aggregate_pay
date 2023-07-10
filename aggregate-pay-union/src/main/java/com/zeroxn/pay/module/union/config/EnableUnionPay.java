package com.zeroxn.pay.module.union.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 下午1:29
 * @Description: 开启云闪付的启动注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UnionPayAutoConfiguration.class)
public @interface EnableUnionPay {
}
