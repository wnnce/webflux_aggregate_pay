package com.zeroxn.pay.core.exception;

/**
 * @Author: lisang
 * @DateTime: 2023/6/12 下午6:19
 * @Description: 支付业务异常
 */
public class PayServiceException extends PayException{
    public PayServiceException(String message) {
        super(message);
    }
}
