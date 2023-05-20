package com.zeroxn.pay.core.handler;

import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;

public interface PayHandler {
    <T> T handlerConfirmOrder(PayParams param, PayMethod method, Class<T> clazz);
    <T> T handlerCloseOrder(String orderId, PayMethod method, Class<T> clazz);
    <T> T handlerQueryOrder(String orderId, PayMethod method, Class<T> clazz);
    <T> T handlerOrderRefund(PayParams param, Class<T> clazz);
    <T> T handlerQueryRefund(String orderId, String orderRefundId, Class<T> clazz);
}
