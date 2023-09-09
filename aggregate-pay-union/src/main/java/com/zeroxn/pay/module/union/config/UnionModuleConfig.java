package com.zeroxn.pay.module.union.config;

import com.zeroxn.pay.core.register.CertRegistry;
import com.zeroxn.pay.core.register.ModuleRegistry;
import com.zeroxn.pay.core.config.PayModuleConfigurer;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 上午10:48
 * @Description:
 */
public class UnionModuleConfig implements PayModuleConfigurer {
    private final UnionPayProperties properties;
    public UnionModuleConfig(UnionPayProperties properties) {
        this.properties = properties;
    }
    @Override
    public void addModule(ModuleRegistry registry) {
        registry.add("union");
    }

    @Override
    public void addCert(CertRegistry registry) {
        registry.add(properties.getSignCertPwd(), properties.getSignCertPath(), properties.getEncryptCertPath(),
                properties.getMiddleCertPath(), properties.getRootCertPath());
    }
}
