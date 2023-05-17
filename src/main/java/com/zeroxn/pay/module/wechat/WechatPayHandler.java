package com.zeroxn.pay.module.wechat;

import com.wechat.pay.java.service.payments.model.Transaction;
import com.zeroxn.pay.core.entity.PayParam;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.core.exception.WechatPayException;
import com.zeroxn.pay.core.handler.PayHandler;
import com.zeroxn.pay.core.utils.BaseUtils;
import com.zeroxn.pay.module.wechat.service.h5.WechatPayH5Service;
import com.zeroxn.pay.module.wechat.service.jsapi.WechatPayJsapiService;
import com.zeroxn.pay.module.wechat.service.refund.WechatRefundService;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @Author: lisang
 * @DateTime: 2023/4/28 13:09
 * @Description: 微信支付交易请求处理 负责参数校验 调用下层Service方法和响应参数处理
 */
@Slf4j
public class WechatPayHandler implements PayHandler {
    /**
     * 下订单 下订单之前先查询当前订单号是否已经存在
     * @param param 封装下单参数
     * @param method 订单的支付方式
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    @Override
    public <T> T handlerConfirmOrder(PayParam param, PayMethod method, Class<T> clazz) {
        Transaction transaction = null;
        // 先查询订单
        if (Objects.requireNonNull(method) == PayMethod.APPLETS) {
            transaction = WechatPayJsapiService.queryOrderByOrderId(param.getOrderId());
        } else {
            transaction = WechatPayH5Service.queryOrderByOrderId(param.getOrderId());
        }
        // 判断订单是否存在
        if(transaction != null){
            log.warn("订单已存在，订单号：{}，订单状态：{}", param.getOrderId(), transaction.getTradeState());
            throw new WechatPayException("订单已存在", param.getOrderId());
        }
        // 下单
        switch (method){
            case APPLETS:{
                if(BaseUtils.checkObjectFieldIsNull(param, "orderId", "description", "openId", "total")){
                    throw new WechatPayException("微信支付小程序下单参数错误");
                }
                return (T) WechatPayJsapiService.confirmOrder(param);
            }
            default:{
                if(BaseUtils.checkObjectFieldIsNull(param, "orderId", "description", "ipAddress", "total", "type")){
                    throw new WechatPayException("微信支付H5下单参数错误");
                }
                return (T) WechatPayH5Service.confirmOrder(param);
            }
        }
    }

    /**
     * 关闭订单 微信支付JavaAPI封装程度很高
     * @param orderId 商户系统内的订单id
     * @param method 支付方式 小程序或H5
     */
    @Override
    public void handlerCloseOrder(String orderId, PayMethod method) {
        switch (method){
            case APPLETS:{
                WechatPayJsapiService.closeOrder(orderId);
            }
            default:{
                WechatPayH5Service.closeOrder(orderId);
            }
        }
        log.info("微信关闭订单，订单号：{}", orderId);
    }

    /**
     * 查询订单
     * @param orderId 商户系统内的订单id
     * @param method 订单的支付方式
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    @Override
    public <T> T handlerQueryOrder(String orderId, PayMethod method, Class<T> clazz) {
        switch (method){
            case APPLETS:{
                return (T) WechatPayJsapiService.queryOrderByOrderId(orderId);
            }
            default:{
                return (T) WechatPayH5Service.queryOrderByOrderId(orderId);
            }
        }
    }

    /**
     * 微信支付订单退款
     * @param param 封装退款参数
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    @Override
    public <T> T handlerOrderRefund(PayParam param, Class<T> clazz) {
        if (BaseUtils.checkObjectFieldIsNull(param, "orderId", "orderRefundId", "total", "refundTotal")){
            throw new WechatPayException("微信支付退款参数错误");
        }
        return (T) WechatRefundService.orderRefund(param);
    }

    /**
     * 查询微信支付退款订单
     * @param orderId 商户系统内的订单id
     * @param orderRefundId 商户系统内的订单退款id
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    @Override
    public <T> T handlerQueryRefund(String orderId, String orderRefundId, Class<T> clazz) {
        return (T) WechatRefundService.queryRefund(orderRefundId);
    }
}
