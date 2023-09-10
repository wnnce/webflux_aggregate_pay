package com.zeroxn.pay.core.config;

import com.zeroxn.pay.core.register.CertRegistry;
import com.zeroxn.pay.core.register.ModuleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 上午10:38
 * @Description: 支付项目核心配置类
 */
@Configuration
public class PayAutoConfiguration {
    private final ModuleRegistry moduleRegistry;
    private final CertRegistry certRegistry;

    public PayAutoConfiguration(List<PayModuleConfigurer> configurers) {
        moduleRegistry = new ModuleRegistry();
        certRegistry = new CertRegistry();
        if(!configurers.isEmpty()){
            configurers.forEach(module -> {
                module.addModule(moduleRegistry);
                module.addCert(certRegistry);
            });
        }
    }

    @Bean
    public ModuleRegistry moduleRegistry() {
        return moduleRegistry;
    }

    @Bean
    public CertRegistry certRegistry() {
        return certRegistry;
    }

    @Bean
    public PayCertManager payCertManager() {
        return new PayCertManager(certRegistry);
    }
}
