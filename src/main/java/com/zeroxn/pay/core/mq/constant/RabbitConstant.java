package com.zeroxn.pay.core.mq.constant;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午6:04
 * @Description: RabbitMQ常量类
 */
public class RabbitConstant {
    public static final String EXCHANGE_NAME = "zeroxn.pay";
    public static final String WE_SU_NAME = "wechat.success";
    public static final String WE_RE_NAME = "wechat.refund";
    public static final String AL_SU_NAME = "alipay.success";
    public static final String WE_SU_KEY = "ws";
    public static final String WE_RE_KEY = "wr";
    public static final String AL_SU_KEY = "as";
}
