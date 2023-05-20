package com.zeroxn.pay.module.alipay.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.zeroxn.pay.module.alipay.AlipayPayTemplate;
import com.zeroxn.pay.module.alipay.service.AlipayPayService;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: lisang
 * @DateTime: 2023/5/18 下午8:10
 * @Description: 支付宝支付自动配置类
 */
@Component
@ConditionalOnProperty(prefix = "pay", name = "alipay.enable", havingValue = "true")
@EnableConfigurationProperties(AlipayPayConfig.class)
public class AlipayPayAutoConfig {
    @Bean
    @ConditionalOnClass(AlipayPayConfig.class)
    public AlipayClient alipayClient(@NotNull AlipayPayConfig alipayPayConfig) throws AlipayApiException {
        AlipayConfig config = new AlipayConfig();
        config.setServerUrl(alipayPayConfig.getServerUrl());
        config.setAppId(alipayPayConfig.getAppId());
        config.setPrivateKey(alipayPayConfig.getPrivateKey());
        config.setAlipayPublicKey(alipayPayConfig.getPublicKey());
        config.setSignType(alipayPayConfig.getSignType());
        config.setCharset(alipayPayConfig.getCharSet());
        return new DefaultAlipayClient(config);
    }
    @Bean
    @ConditionalOnClass(AlipayClient.class)
    public AlipayPayService alipayPayService(AlipayClient alipayClient, AlipayPayConfig alipayPayConfig){
        return new AlipayPayService(alipayClient, alipayPayConfig);
    }
    @Bean
    @ConditionalOnClass(AlipayPayService.class)
    public AlipayPayTemplate alipayPayHandler(AlipayPayService alipayPayService){
        return new AlipayPayTemplate(alipayPayService);
    }
}
