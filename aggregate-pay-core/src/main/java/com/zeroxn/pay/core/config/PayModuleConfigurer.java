package com.zeroxn.pay.core.config;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午11:05
 * @Description: 支付模块配置接口
 */
public interface PayModuleConfigurer {
    default void addModule(ModuleRegistry registry) {};
}
