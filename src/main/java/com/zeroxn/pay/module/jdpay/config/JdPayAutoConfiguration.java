package com.zeroxn.pay.module.jdpay.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 上午10:52
 * @Description: 京东支付自动配置类
 */
@Configuration
@AutoConfigureBefore(JdPayAutoConfiguration.class)
@EnableConfigurationProperties(JdPayProperties.class)
@ConditionalOnProperty(value = "pay.jd.enable", havingValue = "true")
public class JdPayAutoConfiguration {

}
