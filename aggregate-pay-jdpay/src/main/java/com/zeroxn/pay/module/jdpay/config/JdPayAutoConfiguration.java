package com.zeroxn.pay.module.jdpay.config;

import com.zeroxn.pay.core.config.PayAutoConfiguration;
import com.zeroxn.pay.module.jdpay.business.JdPayBusiness;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 上午10:52
 * @Description: 京东支付自动配置类
 */
@AutoConfigureBefore(PayAutoConfiguration.class)
@EnableConfigurationProperties(JdPayProperties.class)
public class JdPayAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public JdPayModuleConfig jdPayModuleConfig(JdPayProperties properties) {
        return new JdPayModuleConfig(properties);
    }
    @Bean
    public JdPayBusiness jdPayBusiness(JdPayProperties properties, RestTemplate restTemplate) {
        return new JdPayBusiness(properties, restTemplate);
    }
}
