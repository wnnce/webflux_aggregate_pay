package com.zeroxn.pay.module.wechat.business.h5;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.service.payments.h5.H5Service;
import com.wechat.pay.java.service.payments.h5.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.module.wechat.config.WechatPayProperties;
import org.jetbrains.annotations.NotNull;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 12:00
 * @Description: 微信支付H5下单服务类 适用于手机端浏览器下单
 */
public class WechatPayH5Business {
    private final H5Service service;
    private final WechatPayProperties wechatProperties;
    public WechatPayH5Business(@NotNull Config config,@NotNull WechatPayProperties wechatProperties){
        this.wechatProperties = wechatProperties;
        this.service = new H5Service.Builder().config(config).build();
    }

    /**
     * h5关闭订单
     * @param orderId 商户系统内的订单id
     */
    public void closeOrder(String orderId){
        CloseOrderRequest request = new CloseOrderRequest();
        request.setMchid(wechatProperties.getMerchantId());
        request.setOutTradeNo(orderId);
        service.closeOrder(request);
    }

    /**
     * 微信支付H5平台下单
     * @param param 封装H5下单参数
     * @return h5下单需要的信息
     */
    public PrepayResponse confirmOrder(PayParams param){
        Amount amount = new Amount();
        amount.setTotal(param.getWechatTotal());
        amount.setCurrency(wechatProperties.getCurrency());

        H5Info h5Info = new H5Info();
        h5Info.setType(param.getType());
        h5Info.setAppName(param.getAppName());
        h5Info.setAppUrl(param.getAppUrl());
        h5Info.setBundleId(param.getBundleId());
        h5Info.setPackageName(param.getPackageName());
        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setPayerClientIp(param.getIpAddress());
        sceneInfo.setH5Info(h5Info);

        PrepayRequest request = new PrepayRequest();
        request.setAppid(wechatProperties.getAppId());
        request.setMchid(wechatProperties.getMerchantId());
        request.setDescription(param.getDescription());
        request.setOutTradeNo(param.getOrderId());
        request.setNotifyUrl(wechatProperties.getSuccessNotifyUrl());
        request.setAmount(amount);
        request.setSceneInfo(sceneInfo);
        return service.prepay(request);
    }

    /**
     * h5通过商户平台的订单id查询订单
     * @param orderId 商户平台内的订单id
     * @return 订单详细信息
     */
    public Transaction queryOrderByOrderId(String orderId){
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(wechatProperties.getMerchantId());
        request.setOutTradeNo(orderId);
        return service.queryOrderByOutTradeNo(request);
    }
}
