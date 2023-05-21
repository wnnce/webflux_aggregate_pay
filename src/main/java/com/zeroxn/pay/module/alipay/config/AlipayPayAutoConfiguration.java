package com.zeroxn.pay.module.alipay.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.zeroxn.pay.module.alipay.AlipayPayTemplate;
import com.zeroxn.pay.module.alipay.business.AlipayPayBusiness;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lisang
 * @DateTime: 2023/5/18 下午8:10
 * @Description: 支付宝支付自动配置类
 */

@Configuration
@AutoConfigureBefore(AlipayPayAutoConfiguration.class)
@ConditionalOnProperty(value = "pay.alipay.enable", havingValue = "true")
@EnableConfigurationProperties(AlipayPayProperties.class)
public class AlipayPayAutoConfiguration {
    @Bean
    @ConditionalOnClass(AlipayPayProperties.class)
    public AlipayClient alipayClient(@NotNull AlipayPayProperties alipayPayProperties) throws AlipayApiException {
        AlipayConfig config = new AlipayConfig();
        config.setServerUrl(alipayPayProperties.getServerUrl());
        config.setAppId(alipayPayProperties.getAppId());
        config.setPrivateKey(alipayPayProperties.getPrivateKey());
        config.setAlipayPublicKey(alipayPayProperties.getPublicKey());
        config.setSignType(alipayPayProperties.getSignType());
        config.setCharset(alipayPayProperties.getCharSet());
        return new DefaultAlipayClient(config);
    }
    @Bean
    @ConditionalOnClass(AlipayClient.class)
    public AlipayPayBusiness alipayPayService(AlipayClient alipayClient, AlipayPayProperties alipayPayProperties){
        return new AlipayPayBusiness(alipayClient, alipayPayProperties);
    }
    @Bean
    @ConditionalOnClass(AlipayPayBusiness.class)
    public AlipayPayTemplate alipayPayHandler(AlipayPayBusiness alipayPayBusiness){
        return new AlipayPayTemplate(alipayPayBusiness);
    }
}
