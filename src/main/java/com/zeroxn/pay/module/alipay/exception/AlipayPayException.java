package com.zeroxn.pay.module.alipay.exception;

import com.zeroxn.pay.core.exception.PayException;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 16:39
 * @Description: 支付宝支付异常
 */
public class AlipayPayException extends PayException {
    public AlipayPayException(String message) {
        super(message);
    }
    public AlipayPayException(String message, String orderId){
        super(message);
    }
}
