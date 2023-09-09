package com.zeroxn.pay.module.jdpay.config;

import com.zeroxn.pay.core.register.ModuleRegistry;
import com.zeroxn.pay.core.config.PayModuleConfigurer;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 15:10:31
 * @Description: 京东支付模块配置类
 */
public class JdPayModuleConfig implements PayModuleConfigurer {
    @Override
    public void addModule(ModuleRegistry registry) {
        registry.add("jdpay");
    }
}
