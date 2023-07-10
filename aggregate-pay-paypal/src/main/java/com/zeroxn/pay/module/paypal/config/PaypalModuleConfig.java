package com.zeroxn.pay.module.paypal.config;

import com.zeroxn.pay.core.config.ModuleRegistry;
import com.zeroxn.pay.core.config.PayModuleConfigurer;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 下午3:45
 * @Description:
 */
public class PaypalModuleConfig implements PayModuleConfigurer {
    @Override
    public void addModule(ModuleRegistry registry) {
        registry.addModule("paypal");
    }
}
