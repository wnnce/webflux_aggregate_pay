package com.zeroxn.pay.module.union.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 上午11:47
 * @Description: 云闪付自动配置类
 */
@Configuration
@AutoConfigureBefore(UnionPayAutoConfiguration.class)
@EnableConfigurationProperties(UnionPayProperties.class)
@ConditionalOnProperty(value = "pay.union", havingValue = "true")
public class UnionPayAutoConfiguration {
}
