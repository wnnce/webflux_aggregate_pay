package com.zeroxn.pay.module.alipay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.zeroxn.pay.core.entity.PayParam;
import com.zeroxn.pay.module.alipay.config.AlipayPayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 15:41
 * @Description:
 */
public class AlipayPayService {
    private final Logger logger = LoggerFactory.getLogger(AlipayPayService.class);
    private final AlipayClient alipayClient;
    private final AlipayPayConfig alipayConfig;
    public AlipayPayService(AlipayClient alipayClient, AlipayPayConfig alipayConfig){
        this.alipayClient = alipayClient;
        this.alipayConfig = alipayConfig;
    }

    /**
     * 支付宝小程序下单接口 返回小程序下单所需要的参数
     * @param param 封装请求参数
     * @return 返回支付宝小程序下单所需的数据
     */
    public AlipayTradeCreateResponse appletsConfirmOrder(PayParam param){
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(param.getOrderId());
        model.setTotalAmount(param.getAlipayTotal().toString());
        model.setSubject(param.getDescription());
        model.setBuyerId(param.getUserId());
        model.setSellerId(alipayConfig.getSellerId());
        request.setBizModel(model);
        try{
            return alipayClient.execute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            logger.error("支付宝小程序下单接口请求失败，订单号：{}，错误消息：{}", param.getOrderId(), ex.getMessage());
            return null;
        }
    }

    /**
     * 支付宝手机网站下单 调起支付宝APP支付
     * @param param 封装下单参数
     */
    public AlipayTradeWapPayResponse wapConfirmOrder( PayParam param) {
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setReturnUrl(param.getReturnUrl());
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(param.getOrderId());
        model.setTotalAmount(param.getAlipayTotal().toString());
        model.setSubject(param.getDescription());
        model.setProductCode("QUICK_WAP_WAY");
        model.setQuitUrl(param.getQuitUrl());
        request.setBizModel(model);
        try{
            return alipayClient.pageExecute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            logger.error("支付宝手机网站下单接口请求失败，订单号：{}，错误消息：{}", param.getOrderId(), ex.getMessage());
            return null;
        }
    }

    /**
     * 支付宝电脑网站调起支付
     * @param param 封装下单参数
     * @return 电脑端下单所需的参数
     */
    public AlipayTradePagePayResponse desktopConfirmOrder(PayParam param){
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(param.getOrderId());
        model.setTotalAmount(param.getAlipayTotal().toString());
        model.setSubject(param.getDescription());
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        request.setBizModel(model);
        try{
            return alipayClient.pageExecute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            logger.error("支付宝电脑网站下单接口请求失败，订单号：{}，错误消息：{}", param.getOrderId(), ex.getMessage());
            return null;
        }
    }

    /**
     * 通过商户订单id查询订单信息
     * @param orderId 商户订单id
     * @return 订单详细信息
     */
    public AlipayTradeQueryResponse queryOrderByOrderId(String orderId){
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(orderId);
        request.setBizModel(model);
        try{
            return alipayClient.execute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            logger.error("支付宝查询订单接口请求失败，订单号：{}", orderId);
            return null;
        }
    }

    /**
     * 通过商户订单id关闭订单
     * @param orderId 商户订单id
     * @return 返回关闭订单的响应体
     */
    public AlipayTradeCloseResponse closeOrder(String orderId) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setOutTradeNo(orderId);
        request.setBizModel(model);
        try {
            return alipayClient.execute(request);
        } catch (AlipayApiException ex) {
            ex.printStackTrace();
            logger.error("支付宝关闭订单接口请求失败，订单号：{}", orderId);
            return null;
        }
    }

    /**
     * 支付宝订单退款
     * @param param 封装退款参数
     */
    public AlipayTradeRefundResponse refundOrder(PayParam param){
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(param.getOrderId());
        model.setRefundAmount(param.getAlipayRefundTotal().toString());
        model.setOutRequestNo(param.getOrderRefundId());
        request.setBizModel(model);
        try{
            return alipayClient.execute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            logger.error("支付宝订单退款接口请求失败，订单号：{}，退款单号：{}", param.getOrderId(), param.getOrderRefundId());
            return null;
        }
    }

    /**
     * 支付宝查询退款
     * @param orderId 商户系统内的订单id
     * @param orderRefundId 商户系统内的订单退款id
     */
    public AlipayTradeFastpayRefundQueryResponse queryRefund(String orderId, String orderRefundId){
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setOutTradeNo(orderId);
        model.setOutRequestNo(orderRefundId);
        request.setBizModel(model);
        try{
            return alipayClient.execute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            logger.error("支付宝退款订单查询失败，订单号：{}，退款单号：{}", orderId, orderRefundId);
            return null;
        }
    }
}
