package com.zeroxn.pay.core.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: lisang
 * @DateTime: 2023/4/28 13:56
 * @Description: 微信支付异常类
 */
@Slf4j
public class WechatPayException extends PayException{
    public WechatPayException(String message) {
        super(message);
    }
    public WechatPayException(String message, String orderId){
        super(message);
        log.error("抛出微信支付异常，错误消息：{}，订单号：{}", message, orderId);
    }
}
