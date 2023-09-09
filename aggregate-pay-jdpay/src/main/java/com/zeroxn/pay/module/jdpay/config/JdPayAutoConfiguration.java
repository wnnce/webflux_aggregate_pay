package com.zeroxn.pay.module.jdpay.config;

import com.zeroxn.pay.core.config.PayAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 上午10:52
 * @Description: 京东支付自动配置类
 */
@AutoConfigureBefore(PayAutoConfiguration.class)
@EnableConfigurationProperties(JdPayProperties.class)
public class JdPayAutoConfiguration {

    @Bean
    public JdPayModuleConfig jdPayModuleConfig() {
        return new JdPayModuleConfig();
    }
}
