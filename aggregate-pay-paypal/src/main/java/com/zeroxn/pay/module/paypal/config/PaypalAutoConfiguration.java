package com.zeroxn.pay.module.paypal.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午12:39
 * @Description: Paypal自动配置类
 */
@Configuration
@AutoConfigureBefore(PaypalAutoConfiguration.class)
@EnableConfigurationProperties(PaypalProperties.class)
@ConditionalOnProperty(value = "pay.paypal.enable", havingValue = "true")
public class PaypalAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
