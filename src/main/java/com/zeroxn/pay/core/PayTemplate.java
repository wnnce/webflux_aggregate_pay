package com.zeroxn.pay.core;

import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import org.apache.kafka.common.protocol.types.Field;
import reactor.util.annotation.NonNull;

import java.util.Optional;

public interface PayTemplate {
    /**
     * 获取实现支付模板接口的平台名称
     * @return 返回平台名称
     */
    String getPlatformName();
    <T> T confirmOrder(PayParams param, PayMethod method, Class<T> clazz);
    <T> T closeOrder(String orderId, PayMethod method, Class<T> clazz);
    <T> T queryOrder(String orderId, PayMethod method, Class<T> clazz);
    <T> T refundOrder(PayParams param, Class<T> clazz);
    <T> T queryRefundOrder(String orderId, String orderRefundId, Class<T> clazz);
}
