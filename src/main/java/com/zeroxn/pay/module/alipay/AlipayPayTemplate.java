package com.zeroxn.pay.module.alipay;

import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.module.alipay.exception.AlipayPayException;
import com.zeroxn.pay.core.interfaces.PayTemplate;
import com.zeroxn.pay.module.alipay.business.AlipayPayBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/4/28 08:47
 * @Description: 支付宝交易请求处理 负责参数校验 调用Service层方法和响应参数处理
 */
public class AlipayPayTemplate implements PayTemplate {
    private final Logger logger = LoggerFactory.getLogger(AlipayPayTemplate.class);
    private final AlipayPayBusiness alipayBusiness;
    public AlipayPayTemplate(AlipayPayBusiness alipayService){
        this.alipayBusiness = alipayService;
    }
    /**
     * 支付宝下单 下单之前会先通过订单id查询订单 判断订单状态
     * @param param 封装下单参数
     * @param method 订单的支付方式
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    public <T> T confirmOrder(PayParams param, PayMethod method, Class<T> clazz) {
        switch (method) {
            case APPLETS -> {
                AlipayTradeCreateModel model = new AlipayTradeCreateModel();
                model.setOutTradeNo(param.getOrderId());
                model.setTotalAmount(param.getAlipayTotal().toString());
                model.setSubject(param.getDescription());
                model.setBuyerId(param.getUserId());
                return (T) alipayBusiness.appletsConfirmOrder(model);
            }
            case WAP -> {
                AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
                model.setOutTradeNo(param.getOrderId());
                model.setTotalAmount(param.getAlipayTotal().toString());
                model.setSubject(param.getDescription());
                model.setProductCode("QUICK_WAP_WAY");
                model.setQuitUrl(param.getQuitUrl());
                return (T) alipayBusiness.wapConfirmOrder(model);
            }
            case DESKTOP -> {
                AlipayTradePagePayModel model = new AlipayTradePagePayModel();
                model.setOutTradeNo(param.getOrderId());
                model.setTotalAmount(param.getAlipayTotal().toString());
                model.setSubject(param.getDescription());
                model.setProductCode("FAST_INSTANT_TRADE_PAY");
                model.setQrPayMode(param.getQrMode());
                model.setQrcodeWidth(param.getQrWidth());
                return (T) alipayBusiness.desktopConfirmOrder(model);
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
    public <T> T closeOrder(String orderId, PayMethod method, Class<T> clazz) {
        return (T) alipayBusiness.closeOrder(orderId);
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
    public <T> T queryOrder(String orderId, PayMethod method, Class<T> clazz) {
        return (T) alipayBusiness.queryOrderByOrderId(orderId);
    }

    /**
     * 订单退款
     * @param param 封装退款参数
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    @Override
    public <T> T refundOrder(PayParams param, Class<T> clazz) {
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(param.getOrderId());
        model.setRefundAmount(param.getAlipayRefundTotal().toString());
        model.setOutRequestNo(param.getOrderRefundId());
        model.setRefundReason(param.getRefundDescription());
        return (T) alipayBusiness.refundOrder(model);
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
    public <T> T queryRefundOrder(String orderId, String orderRefundId, Class<T> clazz) {
        return (T) alipayBusiness.queryRefund(orderId, orderRefundId);
    }

    public boolean notifySignVerified(Map<String, String> paramsMap){
        return alipayBusiness.signVerified(paramsMap);
    }
}