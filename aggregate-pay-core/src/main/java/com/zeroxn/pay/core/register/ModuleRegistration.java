package com.zeroxn.pay.core.register;

import org.springframework.util.Assert;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午11:07
 * @Description: 支付模块注册实例
 */
public class ModuleRegistration {
    private final String moduleName;

    public ModuleRegistration (String moduleName) {
        Assert.notNull(moduleName, "支付模块名称不能为空");
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }
}
