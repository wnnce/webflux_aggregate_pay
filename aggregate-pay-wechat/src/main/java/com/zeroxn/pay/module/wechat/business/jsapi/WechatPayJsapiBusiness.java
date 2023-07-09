package com.zeroxn.pay.module.wechat.business.jsapi;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.module.wechat.config.WechatPayProperties;
import org.jetbrains.annotations.NotNull;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 12:00
 * @Description: 微信支付JSAPI下单服务类 对应小程序下单
 */
public class WechatPayJsapiBusiness {
    private final JsapiServiceExtension service;
    private final WechatPayProperties wechatProperties;
    public WechatPayJsapiBusiness(@NotNull Config config, @NotNull WechatPayProperties wechatProperties){
        this.wechatProperties = wechatProperties;
        this.service = new JsapiServiceExtension.Builder()
                .config(config)
                .signType(wechatProperties.getSignType())
                .build();
    }
    /**
     * jsapi关闭订单
     * @param orderId 商户系统内的订单id
     */
    public void closeOrder(String orderId) {
        CloseOrderRequest request = new CloseOrderRequest();
        request.setMchid(wechatProperties.getMerchantId());
        request.setOutTradeNo(orderId);
        service.closeOrder(request);
    }

    /**
     * JSAPI下单并返回小程序下单所需的参数 需要接入微信小程序用户登录才可用
     * @param param 封装下单请求参数
     * @return 返回小程序支付所需的参数
     */
    public PrepayWithRequestPaymentResponse confirmOrder(PayParams param) {
        // 金额
        Amount amount = new Amount();
        amount.setTotal(param.getWechatTotal());
        amount.setCurrency(wechatProperties.getCurrency());

        Payer payer = new Payer();
        payer.setOpenid(param.getUserId());

        PrepayRequest request = new PrepayRequest();
        request.setAppid(wechatProperties.getAppId());
        request.setMchid(wechatProperties.getMerchantId());
        request.setOutTradeNo(param.getOrderId());
        request.setDescription(param.getDescription());
        request.setNotifyUrl(wechatProperties.getSuccessNotifyUrl());
        request.setAmount(amount);
        request.setPayer(payer);
        return service.prepayWithRequestPayment(request);
    }

    /**
     * 通过商户订单号查询微信支付后台订单
     * @param orderId 商户订单号
     * @return 订单详细信息或空
     */
    public Transaction queryOrderByOrderId(String orderId) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(wechatProperties.getMerchantId());
        request.setOutTradeNo(orderId);
        return service.queryOrderByOutTradeNo(request);
    }
}
