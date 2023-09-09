package com.zeroxn.pay.module.wechat.config;

import com.zeroxn.pay.core.register.ModuleRegistry;
import com.zeroxn.pay.core.config.PayModuleConfigurer;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 上午10:55
 * @Description:
 */
public class WechatModuleConfig implements PayModuleConfigurer {
    @Override
    public void addModule(ModuleRegistry registry) {
        registry.add("wechat");
    }
}
