package com.zeroxn.pay.module.alipay;

import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.zeroxn.pay.core.entity.PayParam;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.core.exception.AlipayPayException;
import com.zeroxn.pay.core.handler.PayHandler;
import com.zeroxn.pay.core.utils.BaseUtils;
import com.zeroxn.pay.module.alipay.service.AlipayPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: lisang
 * @DateTime: 2023/4/28 08:47
 * @Description: 支付宝交易请求处理 负责参数校验 调用Service层方法和响应参数处理
 */
public class AlipayPayHandler implements PayHandler {
    private final Logger logger = LoggerFactory.getLogger(AlipayPayHandler.class);
    private final AlipayPayService alipayService;
    public AlipayPayHandler(AlipayPayService alipayService){
        this.alipayService = alipayService;
    }
    /**
     * 支付宝下单 下单之前会先通过订单id查询订单 判断订单状态
     * @param param 封装下单参数
     * @param method 订单的支付方式
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    public <T> T handlerConfirmOrder(PayParam param, PayMethod method, Class<T> clazz) {
        AlipayTradeQueryResponse queryResponse = handlerQueryOrder(param.getOrderId(), method,
                AlipayTradeQueryResponse.class);
        if(queryResponse != null){
            logger.warn("订单已存在，订单号：{}", param.getOrderId());
            throw new AlipayPayException("订单已存在", param.getOrderId());
        }
        logger.info("订单未创建，可以下单。订单号：{}", param.getOrderId());
        switch (method){
            case APPLETS:{
                if(BaseUtils.checkObjectFieldIsNull(param, "orderId", "description", "userId", "total")){
                    throw new AlipayPayException("支付宝小程序下单参数错误");
                }
                return (T) alipayService.appletsConfirmOrder(param);
            }
            case WAP:{
                if(BaseUtils.checkObjectFieldIsNull(param, "orderId", "description", "quitUrl", "total", "returnUrl")){
                    throw new AlipayPayException("支付宝WAP下单参数错误");
                }
                return (T) alipayService.wapConfirmOrder(param);
            }
            case DESKTOP:{
                if(BaseUtils.checkObjectFieldIsNull(param, "orderId", "description", "total")){
                    throw new AlipayPayException("支付宝DESKTOP下单参数错误");
                }
                return (T) alipayService.desktopConfirmOrder(param);
            }
        }
        throw new AlipayPayException("未知异常", param.getOrderId());
    }


    /**
     * 关闭订单 支付宝关闭订单使用的是统一接口 这里method参数不使用
     * @param orderId 商户系统内的订单id
     * @param method 订单的支付方式
     */
    @Override
    public void handlerCloseOrder(String orderId, PayMethod method) {
        AlipayTradeCloseResponse response = alipayService.closeOrder(orderId);
        // 判断调用是否成功
        if (response == null || !response.isSuccess()){
            throw new AlipayPayException("关闭订单接口调用失败", orderId);
        }
        // 1000 是操作成功状态码
        if(!"1000".equals(response.getCode())){
            switch (response.getSubCode()){
                case "ACQ.TRADE_NOT_EXIST": {
                    logger.error("关闭订单异常，该订单不存在，订单号：{}", orderId);
                    throw new AlipayPayException("订单号不存在", orderId);
                }
                case "ACQ.TRADE_STATUS_ERROR": {
                    logger.error("关闭订单异常，订单状态错误，订单号：{}", orderId);
                    throw new AlipayPayException("订单状态不合法", orderId);
                }
                default:{
                    logger.error("关闭订单未知异常，错误码：{}，业务错误码：{}", response.getCode(), response.getSubCode());
                    throw new AlipayPayException("关闭订单未知异常", orderId);
                }
            }
        }else {
            logger.info("关闭订单成功，订单号：{}", orderId);
        }
    }

    /**
     * 查询订单 支付宝统一接口 不使用method参数
     * @param orderId 商户系统内的订单id
     * @param method 订单的支付方式
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    @Override
    public <T> T handlerQueryOrder(String orderId, PayMethod method, Class<T> clazz) {
        AlipayTradeQueryResponse response = alipayService.queryOrderByOrderId(orderId);
        if(response == null){
            logger.error("支付宝订单查询失败，订单号{}", orderId);
            return null;
        }
        return (T)response;
    }

    /**
     * 订单退款
     * @param param 封装退款参数
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    @Override
    public <T> T handlerOrderRefund(PayParam param, Class<T> clazz) {
        if(BaseUtils.checkObjectFieldIsNull(param, "orderId", "orderRefundId", "total", "refundTotal")){
            throw new AlipayPayException("支付宝退款参数错误");
        }
        AlipayTradeRefundResponse response = alipayService.refundOrder(param);
        if(response != null && response.isSuccess()){
            if("1000".equals(response.getCode())){
                return (T) response;
            }
            switch (response.getSubCode()){
                case "ACQ.SELLER_BALANCE_NOT_ENOUGH": {
                    logger.error("退款卖家余额不足，订单号：{}", param.getOrderId());
                    throw new AlipayPayException("余额不足", param.getOrderId());
                }
                case "ACQ.REFUND_AMT_NOT_EQUAL_TOTAL": {
                    logger.error("退款金额超限，订单号：{}，退款金额：{}", param.getOrderId(), param.getAlipayRefundTotal());
                    throw new AlipayPayException("退款金额超限", param.getOrderId());
                }
                case "ACQ.TRADE_NOT_EXIST": {
                    logger.error("退款订单不存在，订单号：{}", param.getOrderId());
                    throw new AlipayPayException("退款订单不存在", param.getOrderId());
                }
                default:{
                    logger.error("退款未知异常，订单号：{}，退款单号：{}，错误码：{}，业务错误码：{}",
                            param.getOrderId(), param.getOrderRefundId(), response.getCode(), response.getSubCode());
                    throw new AlipayPayException("退款未知异常", param.getOrderId());
                }
            }
        }
        logger.error("退款接口请求失败，订单号：{}", param.getOrderId());
        throw new AlipayPayException("退款请求失败", param.getOrderId());
    }

    /**
     * 查询退款订单
     * @param orderId 商户系统内的订单id
     * @param orderRefundId 商户系统内的订单退款id
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    @Override
    public <T> T handlerQueryRefund(String orderId, String orderRefundId, Class<T> clazz) {
        AlipayTradeFastpayRefundQueryResponse response = alipayService.queryRefund(orderId, orderRefundId);
        if(response != null && response.isSuccess()){
            if("1000".equals(response.getCode())){
                return (T) response;
            }
            switch (response.getSubCode()){
                case "ACQ.TRADE_NOT_EXIST": {
                    logger.warn("订单不存在，订单号：{}，退款请求号：{}", orderId, orderRefundId);
                    throw new AlipayPayException("订单号或退款请求号不存在", orderId);
                }
                case "TRADE_NOT_EXIST": {
                    logger.warn("退款不存在, 订单号：{}，退款请求号：{}", orderId, orderRefundId);
                    throw new AlipayPayException("订单没有退款", orderId);
                }
                default: {
                    logger.error("查询退款未知异常，订单号：{}，退款请求号：{}，错误码：{}，业务错误码：{}",
                            orderId, orderRefundId, response.getCode(), response.getSubCode());
                }
            }
        }
        logger.error("查询退款请求失败，订单号：{}", orderId);
        throw new AlipayPayException("查询退款请求失败", orderId);
    }
}
