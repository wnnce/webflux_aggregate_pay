package com.zeroxn.pay.module.union.exception;

import com.zeroxn.pay.core.exception.PayException;

/**
 * @Author: lisang
 * @DateTime: 2023/6/12 下午6:16
 * @Description: 云闪付支付异常类
 */
public class UnionPayException extends PayException {
    public UnionPayException(String message) {
        super(message);
    }
}
