package com.zeroxn.pay.core.enums;

/**
 * @Author: lisang
 * @DateTime: 2023/4/28 08:24
 * @Description: 支付平台枚举类 目前仅支持微信（WECHAT）和支付宝（ALIPAY）
 */
public enum PayPlatform {
    WECHAT ("wechat"),
    ALIPAY ("alipay"),
    UNION ("union");
    private final String value;
    PayPlatform(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
