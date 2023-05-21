package com.zeroxn.pay.module.wechat.exception;

import com.zeroxn.pay.core.exception.PayException;

/**
 * @Author: lisang
 * @DateTime: 2023/4/28 13:56
 * @Description: 微信支付异常类
 */
public class WechatPayException extends PayException {
    public WechatPayException(String message) {
        super(message);
    }
    public WechatPayException(String message, String orderId){
        super(message);
    }
}
