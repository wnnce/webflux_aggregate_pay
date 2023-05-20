package com.zeroxn.pay.module.wechat.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: lisang
 * @DateTime: 2023/5/20 下午7:23
 * @Description: 微信支付自动配置类
 */
@Component
@ConditionalOnProperty(prefix = "pay", name = "wechat.enable", havingValue = "true")
@EnableConfigurationProperties(WechatPayConfig.class)
public class WechatPayAutoConfig {
    @Bean
    @ConditionalOnClass(WechatPayConfig.class)
    public Config config(WechatPayConfig payConfig){
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(payConfig.getMerchantId())
                .apiV3Key(payConfig.getApiV3Key())
                .merchantSerialNumber(payConfig.getMerchantSerialNumber())
                .privateKey(payConfig.getPrivateKey())
                .build();
    }

}
