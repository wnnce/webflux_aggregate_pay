package com.zeroxn.pay.module.wechat.exception;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午2:16
 * @Description: 微信支付系统异常
 */
public class WechatPaySystemException extends WechatPayException{
    public WechatPaySystemException(String message) {
        super(message);
    }

    public WechatPaySystemException(String message, String orderId) {
        super(message, orderId);
    }
}
