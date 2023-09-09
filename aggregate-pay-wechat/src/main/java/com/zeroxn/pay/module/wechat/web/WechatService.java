package com.zeroxn.pay.module.wechat.web;

import com.wechat.pay.java.service.payments.h5.model.PrepayResponse;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.RefundNotification;
import com.zeroxn.pay.core.PayTemplate;
import com.zeroxn.pay.core.amqp.PayMQTemplate;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;
import com.zeroxn.pay.core.exception.PayServiceException;
import com.zeroxn.pay.module.wechat.business.parser.WechatNotifyParser;
import com.zeroxn.pay.module.wechat.config.ConditionalOnWechat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午12:45
 * @Description: 微信支付Service层
 */
@Service
@Conditional(ConditionalOnWechat.class)
public class WechatService {
    private static final Logger logger = LoggerFactory.getLogger(WechatService.class);
    private final PayTemplate payTemplate;
    private final WechatNotifyParser notifyParser;
    private final PayMQTemplate mqTemplate;
    public WechatService(@Qualifier("wechatPayTemplate") PayTemplate payTemplate, WechatNotifyParser notifyParser, PayMQTemplate mqTemplate){
        this.payTemplate = payTemplate;
        this.notifyParser = notifyParser;
        this.mqTemplate = mqTemplate;
    }

    /**
     * 微信支付H5下单
     * @param paramDTO 下单参数
     * @return 返回H5跳转下单的Url
     */
    public String wechatH5Pay(WechatParamDTO paramDTO){
        PayParams params = new PayParams.BuilderWechatH5()
                .setOrderId(paramDTO.getOrderId())
                .setTotal(paramDTO.getTotal())
                .setDescription(paramDTO.getDescription())
                .setIpAddress(paramDTO.getIpAddress())
                .setType(paramDTO.getType())
                .setAppName(paramDTO.getAppName())
                .setAppUrl(paramDTO.getAppUrl())
                .setBundleId(paramDTO.getBundleId())
                .setPackageName(paramDTO.getPackageName())
                .build();
        PrepayResponse response = payTemplate.confirmOrder(params, PayMethod.WAP, PrepayResponse.class);
        logger.info("微信支付H5下单成功，订单号：{}", params.getOrderId());
        return response.getH5Url();
    }

    /**
     * 微信支付Jsapi下单
     * @param paramDTO 下单参数
     * @return 返回Jsapi（小程序）下单所需的参数
     */
    public PrepayWithRequestPaymentResponse wechatJsapiPay(WechatParamDTO paramDTO){
        PayParams params = new PayParams.BuilderWechatApplets()
                .setOrderId(paramDTO.getOrderId())
                .setTotal(paramDTO.getTotal())
                .setOpenId(paramDTO.getUserId())
                .setDescription(paramDTO.getDescription())
                .build();
        PrepayWithRequestPaymentResponse response = payTemplate.confirmOrder(params, PayMethod.APPLETS, PrepayWithRequestPaymentResponse.class);
        logger.info("微信支付Jsapi下单成功，订单号：{}", params.getOrderId());
        return response;
    }

    /**
     * 通过商户系统内的订单号查询微信支付订单
     * @param method 订单下单方式 1：jsapi 2：H5
     * @param orderId 商户系统内的订单ID
     * @return 返回订单详细信息
     */
    public Transaction queryWechatOrder(Integer method, String orderId){
        PayMethod payMethod = methodForMat(method);
        Transaction transaction = payTemplate.queryOrder(orderId, payMethod, Transaction.class);
        logger.info("微信支付订单查询成功，订单号：{}", orderId);
        return transaction;
    }

    /**
     * 微信支付关闭订单
     * @param method 订单下单方式
     * @param orderId 商户系统内的订单ID
     * @return 关闭成功返回订单ID
     */
    public String wechatOrderClose(Integer method, String orderId){
        PayMethod payMethod = methodForMat(method);
        payTemplate.closeOrder(orderId, payMethod, null);
        logger.info("微信支付关闭订单成功，订单号：{}", orderId);
        return orderId;
    }

    /**
     * 微信支付订单退款
     * @param paramDTO 退款参数
     * @return 返回退款详情
     */
    public Refund wechatOrderRefund(WechatParamDTO paramDTO){
        if(paramDTO.getRefundTotal() > paramDTO.getTotal()){
            throw new PayServiceException("订单退款金额不能大于订单总金额");
        }
        PayParams params = new PayParams.BuilderRefund()
                .setOrderId(paramDTO.getOrderId())
                .setOrderRefundId(paramDTO.getRefundId())
                .setTotal(paramDTO.getTotal())
                .setRefundTotal(paramDTO.getRefundTotal())
                .setRefundDescription(paramDTO.getReason())
                .build();
        Refund refund = payTemplate.refundOrder(params, Refund.class);
        logger.info("微信支付订单退款成功，订单号：{}，退款金额：{}", params.getOrderId(), params.getRefundTotal());
        return refund;
    }

    /**
     * 微信支付查询退款订单详情
     * @param refundId 商户系统内的订单退款ID
     * @return 返回退款订单详情
     */
    public Refund queryWechatRefundOrder(String refundId){
        Refund refund = payTemplate.queryRefundOrder(null, refundId, Refund.class);
        logger.info("微信支付查询退款订单成功，退款单号：{}", refundId);
        return refund;
    }

    /**
     * 微信支付成异步通知处理，先解密通知然后将支付成功的订单添加到消息队列中
     * @return 解密成功返回true 否则false
     */
    public boolean wechatSuccessNotify(String signature, String signType, String nonce, String timestamp, String serial,
                                     String body){
        Transaction transaction = notifyParser.successNotifyDecrypt(signature, signType, nonce, timestamp, serial, body);
        if (transaction == null){
            return false;
        }
        String orderId = transaction.getOutTradeNo();
        WechatService.logger.info("接收到微信支付成功通知回调，订单号：{}", orderId);
        // 消息队列处理
        mqTemplate.send(PayPlatform.WECHAT, PayResult.SUCCESS, orderId);
        return true;
    }

    /**
     * 微信支付退款成功异步通知处理，也是解密通知然后添加到消息队列
     * @return 解密成功返回true 否则false
     */
    public boolean wechatRefundNotify(String signature, String signType, String nonce, String timestamp, String serial,
                                      String body){
        RefundNotification refundNotification = notifyParser.refundNotifyDecrypt(signature, signType, nonce, timestamp, serial, body);
        if(refundNotification == null){
            return false;
        }
        String refundId = refundNotification.getOutRefundNo();
        WechatService.logger.info("接收到微信退款成功通知回调，退款单号：{}", refundId);
        // 消息队列处理
        mqTemplate.send(PayPlatform.WECHAT, PayResult.REFUND, refundId);
        return true;
    }

    private PayMethod methodForMat(Integer method){
        if (method == null || (method < 0 || method > 2)){
            throw new PayServiceException("微信支付方式参数错误");
        }
        if (method == 1){
            return PayMethod.APPLETS;
        }else {
            return PayMethod.WAP;
        }
    }
}
