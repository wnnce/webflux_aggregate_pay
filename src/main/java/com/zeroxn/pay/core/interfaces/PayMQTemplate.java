package com.zeroxn.pay.core.interfaces;

import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;

/**
 * @Author: lisang
 * @DateTime: 2023/5/20 下午11:37
 * @Description: 消息队列接口
 */
public interface PayMQTemplate {
    void send(PayPlatform platform, PayResult result, Object data);
    String[] getNameAndKey(PayPlatform platform, PayResult result);
}