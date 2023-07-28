package com.zeroxn.pay.core.config;

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

    public PayAutoConfiguration(List<PayModuleConfigurer> configurers) {
        moduleRegistry = new ModuleRegistry();
        if(!CollectionUtils.isEmpty(configurers)){
            configurers.forEach(module -> module.addModule(moduleRegistry));
        }
    }

    @Bean
    public ModuleRegistry moduleRegistry() {
        return moduleRegistry;
    }
}
