package com.zeroxn.pay.core.handler;

import com.zeroxn.pay.core.entity.PayParam;
import com.zeroxn.pay.core.enums.PayMethod;

public interface PayHandler {
    <T> T handlerConfirmOrder(PayParam param, PayMethod method, Class<T> clazz);
    void handlerCloseOrder(String orderId, PayMethod method);
    <T> T handlerQueryOrder(String orderId, PayMethod method, Class<T> clazz);
    <T> T handlerOrderRefund(PayParam param, Class<T> clazz);
    <T> T handlerQueryRefund(String orderId, String orderRefundId, Class<T> clazz);
}
