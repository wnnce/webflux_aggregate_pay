package com.zeroxn.pay.module.alipay.config;

import com.zeroxn.pay.core.register.ModuleRegistry;
import com.zeroxn.pay.core.config.PayModuleConfigurer;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 上午10:56
 * @Description:
 */
public class AlipayModuleConfig implements PayModuleConfigurer {
    @Override
    public void addModule(ModuleRegistry registry) {
        registry.add("alipay");
    }
}
