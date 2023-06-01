package com.zeroxn.pay.module.union.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 上午11:46
 * @Description: 云闪付配置类
 */
@ConfigurationProperties(prefix = "pay.union")
@ConditionalOnProperty(value = "pay.union", havingValue = "true")
public class UnionPayProperties {
}
