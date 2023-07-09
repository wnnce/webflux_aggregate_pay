package com.zeroxn.pay.core.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午11:06
 * @Description: 支付Module注册器
 */
public class ModuleRegistry {
    private final List<ModuleRegistration> registrationList = new ArrayList<>();

    public ModuleRegistry(){

    }

    public void addModule(String moduleName) {
        registrationList.add(new ModuleRegistration(moduleName));
    }

    public List<String> getModuleNames() {
        return registrationList.stream().map(ModuleRegistration::getModuleName).toList();
    }
}
