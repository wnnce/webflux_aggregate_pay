package com.zeroxn.pay.module.alipay.business;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.zeroxn.pay.module.alipay.exception.AlipayPaySystemException;
import com.zeroxn.pay.module.alipay.config.AlipayPayConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 15:41
 * @Description:
 */
public class AlipayPayBusiness {
    private final Logger logger = LoggerFactory.getLogger(AlipayPayBusiness.class);
    private final AlipayClient alipayClient;
    private final AlipayPayConfig alipayConfig;
    public AlipayPayBusiness(AlipayClient alipayClient, AlipayPayConfig alipayConfig){
        this.alipayClient = alipayClient;
        this.alipayConfig = alipayConfig;
    }

    /**
     * 支付宝小程序下单接口 返回小程序下单所需要的参数
     * @param model 支付参数
     * @return 返回支付宝小程序下单所需的数据
     */
    public AlipayTradeCreateResponse appletsConfirmOrder(@NotNull AlipayTradeCreateModel model){
        model.setSellerId(alipayConfig.getSellerId());
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setBizModel(model);
        try{
            return alipayClient.execute(request);
        }catch (AlipayApiException ex){
            logger.error("支付宝小程序下单接口请求失败，错误消息：{}", ex.getMessage());
            throw new AlipayPaySystemException("小程序下单失败");
        }
    }

    /**
     * 支付宝手机网站下单 调起支付宝APP支付
     * @param model 下单参数
     */
    public AlipayTradeWapPayResponse wapConfirmOrder(@NotNull AlipayTradeWapPayModel model) {
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setBizModel(model);
        try{
            return alipayClient.pageExecute(request);
        }catch (AlipayApiException ex){
            logger.error("支付宝手机网站下单接口请求失败，错误消息：{}", ex.getMessage());
            throw new AlipayPaySystemException("手机下单失败");
        }
    }

    /**
     * 支付宝电脑网站调起支付
     * @param model 封装下单参数
     * @return 电脑端下单所需的参数
     */
    public AlipayTradePagePayResponse desktopConfirmOrder(AlipayTradePagePayModel model){
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setBizModel(model);
        try{
            return alipayClient.pageExecute(request);
        }catch (AlipayApiException ex){
            logger.error("支付宝电脑网站下单接口请求失败，错误消息：{}", ex.getMessage());
            throw new AlipayPaySystemException("网站下单失败");
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
            logger.error("支付宝查询订单接口请求失败，错误消息：{}", ex.getMessage());
            throw new AlipayPaySystemException("订单查询失败");
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
            logger.error("支付宝关闭订单接口请求失败，错误消息：{}", ex.getMessage());
            throw new AlipayPaySystemException("订单关闭失败");
        }
    }

    /**
     * 支付宝订单退款
     * @param model 封装退款参数
     */
    public AlipayTradeRefundResponse refundOrder(AlipayTradeRefundModel model){
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(model);
        try{
            return alipayClient.execute(request);
        }catch (AlipayApiException ex){
            logger.error("支付宝订单退款接口请求失败，错误消息：{}", ex.getMessage());
            throw new AlipayPaySystemException("订单退款失败");
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
            logger.error("支付宝退款订单查询失败，错误消息：{}", ex.getMessage());
            throw new AlipayPaySystemException("退款订单查询失败");
        }
    }
    /**
     * 支付宝异步通知验签方法 异步通过处理参考：https://opendocs.alipay.com/open/270/105902?pathHash=d5cd617e&ref=api
     * @param paramsMap 通知的所有参数
     * @return true：成功 false：失败
     * @throws AlipayApiException 支付宝API调用异常
     */
    public boolean signVerified(Map<String, String> paramsMap) {
        try{
            return AlipaySignature.rsaCheckV1(paramsMap, alipayConfig.getPublicKey(), alipayConfig.getCharSet(),
                    alipayConfig.getSignType());
        }catch (AlipayApiException ex){
            logger.error("支付宝通知验签失败，错误消息：{}", ex.getMessage());
            return false;
        }
    }
}
