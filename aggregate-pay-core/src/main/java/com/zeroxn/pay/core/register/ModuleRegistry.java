package com.zeroxn.pay.core.register;

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

    /**
     * 添加模块
     * @param moduleName 模块名称
     */
    public void add(String moduleName) {
        registrationList.add(new ModuleRegistration(moduleName));
    }

    /**
     * 获取模块列表
     * @return 返回模块名称列表
     */
    public List<String> getModuleNames() {
        return registrationList.stream().map(ModuleRegistration::getModuleName).toList();
    }
}
