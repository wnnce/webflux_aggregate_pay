package com.zeroxn.pay.core;

import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;

public interface PayTemplate {
    <T> T confirmOrder(PayParams param, PayMethod method, Class<T> clazz);
    <T> T closeOrder(String orderId, PayMethod method, Class<T> clazz);
    <T> T queryOrder(String orderId, PayMethod method, Class<T> clazz);
    <T> T refundOrder(PayParams param, Class<T> clazz);
    <T> T queryRefundOrder(String orderId, String orderRefundId, Class<T> clazz);
}
