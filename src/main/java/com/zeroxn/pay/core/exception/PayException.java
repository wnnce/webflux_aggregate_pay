package com.zeroxn.pay.core.exception;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 10:15
 * @Description: 支付异常类
 */
public class PayException extends RuntimeException{
    public PayException(String message){
        super(message);
    }
}
