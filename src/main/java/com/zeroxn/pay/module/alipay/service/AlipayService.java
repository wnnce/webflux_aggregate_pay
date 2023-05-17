package com.zeroxn.pay.module.alipay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.zeroxn.pay.core.entity.PayParam;
import com.zeroxn.pay.module.alipay.constant.AlipayConstant;
import com.zeroxn.pay.module.alipay.utils.AlipayUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 15:41
 * @Description:
 */
@Slf4j
public class AlipayService {
    private static final AlipayClient alipayClient = AlipayConstant.getAlipayClient();

    /**
     * 支付宝小程序下单接口 返回小程序下单所需要的参数
     * @param param 封装请求参数
     * @return 返回支付宝小程序下单所需的数据
     */
    public static AlipayTradeCreateResponse appletsConfirmOrder(PayParam param){
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(AlipayConstant.getSuccessNotifyUrl());
        String bizContent = AlipayUtils.makeBizContent("out_trade_no", param.getOrderId(),
                "total_amount", param.getAlipayTotal(), "subject", param.getDescription(),
                "buyer_id", param.getUserId(), "seller_id", AlipayConstant.getSellerId());
        request.setBizContent(bizContent);
        try{
            return alipayClient.execute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            log.error("支付宝小程序下单接口请求失败，订单号：{}，错误消息：{}", param.getOrderId(), ex.getMessage());
            return null;
        }
    }

    /**
     * 支付宝手机网站下单 调起支付宝APP支付
     * @param param 封装下单参数
     */
    public static AlipayTradeWapPayResponse wapConfirmOrder( PayParam param){
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setNotifyUrl(AlipayConstant.getSuccessNotifyUrl());
        request.setReturnUrl(param.getReturnUrl());
        String bizContent = AlipayUtils.makeBizContent("out_trade_no", param.getOrderId(),
                "total_amount", param.getAlipayTotal(), "subject", param.getDescription(),
                "quit_url", param.getQuitUrl());
        request.setBizContent(bizContent);
        try{
            return alipayClient.pageExecute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            log.error("支付宝手机网站下单接口请求失败，订单号：{}，错误消息：{}", param.getOrderId(), ex.getMessage());
            return null;
        }
    }

    /**
     * 支付宝电脑网站调起支付
     * @param param 封装下单参数
     * @return 电脑端下单所需的参数
     */
    public static AlipayTradePagePayResponse desktopConfirmOrder(PayParam param){
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(AlipayConstant.getSuccessNotifyUrl());
        String bizContent = AlipayUtils.makeBizContent("out_trade_no", param.getOrderId(),
                "total_amount", param.getAlipayTotal(), "subject", param.getDescription(),
                "product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent);
        try{
            return alipayClient.pageExecute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            log.error("支付宝电脑网站下单接口请求失败，订单号：{}，错误消息：{}", param.getOrderId(), ex.getMessage());
            return null;
        }
    }

    /**
     * 通过商户订单id查询订单信息
     * @param orderId 商户订单id
     * @return 订单详细信息
     */
    public static AlipayTradeQueryResponse queryOrderByOrderId(String orderId){
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        String bizContent = AlipayUtils.makeBizContent("out_trade_no", orderId);
        request.setBizContent(bizContent);
        try{
            return alipayClient.execute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            log.error("支付宝查询订单接口请求失败，订单号：{}", orderId);
            return null;
        }
    }

    /**
     * 通过商户订单id关闭订单
     * @param orderId 商户订单id
     * @return 返回关闭订单的响应体
     */
    public static AlipayTradeCloseResponse closeOrder(String orderId) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        String bizContent = AlipayUtils.makeBizContent("out_trade_no", orderId);
        request.setBizContent(bizContent);
        try {
            return alipayClient.execute(request);
        } catch (AlipayApiException ex) {
            ex.printStackTrace();
            log.error("支付宝关闭订单接口请求失败，订单号：{}", orderId);
            return null;
        }
    }

    /**
     * 支付宝订单退款
     * @param param 封装退款参数
     */
    public static AlipayTradeRefundResponse refundOrder(PayParam param){
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        String bizContent = AlipayUtils.makeBizContent("out_trade_no", param.getOrderId(),
                "refund_amount", param.getAlipayRefundTotal(), "out_request_no", param.getOrderRefundId());
        request.setBizContent(bizContent);
        try{
            return alipayClient.execute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            log.error("支付宝订单退款接口请求失败，订单号：{}，退款单号：{}", param.getOrderId(), param.getOrderRefundId());
            return null;
        }
    }

    /**
     * 支付宝查询退款
     * @param orderId 商户系统内的订单id
     * @param orderRefundId 商户系统内的订单退款id
     */
    public static AlipayTradeFastpayRefundQueryResponse queryRefund(String orderId, String orderRefundId){
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        String bizContent = AlipayUtils.makeBizContent("out_trade_no", orderId, "out_request_no", orderRefundId);
        request.setBizContent(bizContent);
        try{
            return alipayClient.execute(request);
        }catch (AlipayApiException ex){
            ex.printStackTrace();
            log.error("支付宝退款订单查询失败，订单号：{}，退款单号：{}", orderId, orderRefundId);
            return null;
        }
    }
}
