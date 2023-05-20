package com.zeroxn.pay.core.enums;

/**
 * @Author: lisang
 * @DateTime: 2023/5/19 下午1:25
 * @Description: 返回响应码枚举类
 */
public enum ResultCode {
    SUCCESS(200),
    REQUEST_FIELD(400),
    SYSTEM_FIELD(500);
    private final int code;
    ResultCode(int code){
        this.code = code;
    }
    public int getCode(){
        return code;
    }
}
