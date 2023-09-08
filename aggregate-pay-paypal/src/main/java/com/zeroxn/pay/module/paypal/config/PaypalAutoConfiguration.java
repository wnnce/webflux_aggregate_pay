package com.zeroxn.pay.module.paypal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeroxn.pay.core.config.PayAutoConfiguration;
import com.zeroxn.pay.module.paypal.business.PaypalBusiness;
import com.zeroxn.pay.module.paypal.scheduled.TokenInterval;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午12:39
 * @Description: Paypal自动配置类
 */
@AutoConfigureBefore(PayAutoConfiguration.class)
@EnableConfigurationProperties(PaypalProperties.class)
public class PaypalAutoConfiguration {

    @Bean
    public PaypalModuleConfig paypalModuleConfig() {
        return new PaypalModuleConfig();
    }
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public PaypalBusiness paypalBusiness(RestTemplate restTemplate, PaypalProperties properties) {
        PaypalBusiness paypalBusiness = new PaypalBusiness(properties, restTemplate);
        // 使用异步线程获取Token
        CompletableFuture.runAsync(paypalBusiness::initAuthorizationToken);
        return paypalBusiness;
    }
    @Bean
    public TokenInterval tokenInterval(PaypalBusiness paypalBusiness) {
        return new TokenInterval(paypalBusiness);
    }
}
