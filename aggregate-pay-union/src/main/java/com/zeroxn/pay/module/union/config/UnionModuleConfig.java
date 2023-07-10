package com.zeroxn.pay.module.union.config;

import com.zeroxn.pay.core.config.ModuleRegistry;
import com.zeroxn.pay.core.config.PayModuleConfigurer;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 上午10:48
 * @Description:
 */
public class UnionModuleConfig implements PayModuleConfigurer {
    @Override
    public void addModule(ModuleRegistry registry) {
        registry.addModule("union");
    }
}
