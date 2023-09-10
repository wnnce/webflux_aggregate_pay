package com.zeroxn.pay.module.jdpay.config;

import com.zeroxn.pay.core.register.CertRegistry;
import com.zeroxn.pay.core.register.ModuleRegistry;
import com.zeroxn.pay.core.config.PayModuleConfigurer;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 15:10:31
 * @Description: 京东支付模块配置类
 */
public class JdPayModuleConfig implements PayModuleConfigurer {
    private final JdPayProperties properties;
    public JdPayModuleConfig(JdPayProperties properties) {
        this.properties = properties;
    }
    @Override
    public void addModule(ModuleRegistry registry) {
        registry.add("jdpay");
    }

    @Override
    public void addCert(CertRegistry registry) {
        registry.addPemCert(true, properties.getPublicKeyPath())
                .addPemCert(false, properties.getPrivateKeyPath());
    }
}
