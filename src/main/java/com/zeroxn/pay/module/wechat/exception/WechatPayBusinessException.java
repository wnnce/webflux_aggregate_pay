package com.zeroxn.pay.module.wechat.exception;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午2:14
 * @Description: 微信支付业务异常
 */
public class WechatPayBusinessException extends WechatPayException{
    public WechatPayBusinessException(String message) {
        super(message);
    }

    public WechatPayBusinessException(String message, String orderId) {
        super(message, orderId);
    }
}
