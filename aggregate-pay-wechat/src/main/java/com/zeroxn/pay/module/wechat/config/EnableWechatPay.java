package com.zeroxn.pay.module.wechat.config;

import com.wechat.pay.java.core.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 下午1:34
 * @Description: 开启微信支付的启动注解
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnClass(Config.class)
@Import(WechatPayAutoConfiguration.class)
public @interface EnableWechatPay {
}
