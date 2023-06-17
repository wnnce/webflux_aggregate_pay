package com.zeroxn.pay.core.enums;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午8:26
 * @Description: 用于推送消息到消息队列时选择支付成功还是退款成功
 */
public enum PayResult {
    SUCCESS ("success"),
    REFUND ("refund");
    private final String value;
    PayResult(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
