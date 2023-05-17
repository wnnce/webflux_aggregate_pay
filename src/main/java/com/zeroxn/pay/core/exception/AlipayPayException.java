package com.zeroxn.pay.core.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 16:39
 * @Description: 支付宝支付异常
 */
@Slf4j
public class AlipayPayException extends PayException{
    public AlipayPayException(String message) {
        super(message);
    }
    public AlipayPayException(String message, String orderId){
        super(message);
        log.error("抛出支付宝支付异常，错误消息：{}，订单号：{}", message, orderId);
    }
}
