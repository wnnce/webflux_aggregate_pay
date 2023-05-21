package com.zeroxn.pay.module.wechat.business.parser;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.RefundNotification;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午3:55
 * @Description: 微信支付通知解密
 */
public class WechatNotifyParser {
    private final NotificationParser notificationParser;
    public WechatNotifyParser(Config config){
        this.notificationParser = new NotificationParser((NotificationConfig) config);
    }

    /**
     * 微信支付成功通知解密
     * @param signature 通知请求头 签名
     * @param signType 通知请求头 签名类型
     * @param nonce 通知请求头 随机字符串
     * @param timestamp 通知请求头 时间戳
     * @param serial 通知请求头
     * @param body 通知请求体
     * @return 解密完成后的对象
     */
    public Transaction successNotifyDecrypt(String signature, String signType, String nonce, String timestamp, String serial,
                                                   String body){
        RequestParam requestParam = makeRequestParam(signature, signType, nonce, timestamp, serial, body);
        return notificationParser.parse(requestParam, Transaction.class);
    }

    /**
     * 微信支付退款成功通知解密
     * @param signature 通知请求头 签名
     * @param signType 通知请求头 签名类型
     * @param nonce 通知请求头 随机字符串
     * @param timestamp 通知请求头 时间戳
     * @param serial 通知请求头
     * @param body 通知请求体
     * @return 解密完成后的对象
     */
    public RefundNotification refundNotifyDecrypt(String signature, String signType, String nonce, String timestamp, String serial,
                                                         String body){
        RequestParam requestParam = makeRequestParam(signature, signType, nonce, timestamp, serial, body);
        return notificationParser.parse(requestParam, RefundNotification.class);
    }

    // 工具方法 生成解密通知需要的 RequestParam 对象
    private RequestParam makeRequestParam(String signature, String signType, String nonce, String timestamp, String serial,
                                                 String body){
        return new RequestParam.Builder()
                .serialNumber(serial)
                .nonce(nonce)
                .timestamp(timestamp)
                .signature(signature)
                .signType(signType)
                .body(body)
                .build();
    }
}
