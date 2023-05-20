package com.zeroxn.pay.module.alipay.exception;

/**
 * @Author: lisang
 * @DateTime: 2023/5/20 下午2:59
 * @Description: 支付宝支付业务异常
 */
public class AlipayPayBusinessException extends AlipayPayException{
    public AlipayPayBusinessException(String message) {
        super(message);
    }

    public AlipayPayBusinessException(String message, String orderId) {
        super(message, orderId);
    }
}
