package com.zeroxn.pay.module.alipay.exception;

/**
 * @Author: lisang
 * @DateTime: 2023/5/20 下午2:59
 * @Description: 支付宝支付系统异常
 */
public class AlipayPaySystemException extends AlipayPayException{
    public AlipayPaySystemException(String message) {
        super(message);
    }

    public AlipayPaySystemException(String message, String orderId) {
        super(message, orderId);
    }
}
