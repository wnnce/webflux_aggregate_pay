package com.zeroxn.pay.module.wechat;

import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.core.interfaces.PayTemplate;
import com.zeroxn.pay.module.wechat.business.h5.WechatPayH5Business;
import com.zeroxn.pay.module.wechat.business.jsapi.WechatPayJsapiBusiness;
import com.zeroxn.pay.module.wechat.business.refund.WechatRefundBusiness;


/**
 * @Author: lisang
 * @DateTime: 2023/4/28 13:09
 * @Description: 微信支付交易请求处理 负责参数校验 调用下层Service方法和响应参数处理
 */
public class WechatPayTemplate implements PayTemplate {
    private final WechatPayH5Business h5Business;
    private final WechatPayJsapiBusiness jsapiBusiness;
    private final WechatRefundBusiness refundBusiness;
    public WechatPayTemplate(WechatPayH5Business h5Business, WechatPayJsapiBusiness jsapiBusiness, WechatRefundBusiness refundBusiness){
        this.h5Business = h5Business;
        this.jsapiBusiness = jsapiBusiness;
        this.refundBusiness = refundBusiness;
    }
    /**
     * 下订单 下订单之前先查询当前订单号是否已经存在
     * @param param 封装下单参数
     * @param method 订单的支付方式
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    @Override
    public <T> T confirmOrder(PayParams param, PayMethod method, Class<T> clazz) {
        switch (method){
            case APPLETS:{
                return (T) jsapiBusiness.confirmOrder(param);
            }
            default:{
                return (T) h5Business.confirmOrder(param);
            }
        }
    }

    /**
     * 关闭订单 微信支付JavaAPI封装程度很高
     * @param orderId 商户系统内的订单id
     * @param method 支付方式 小程序或H5
     */
    @Override
    public <T> T closeOrder(String orderId, PayMethod method, Class<T> clazz) {
        switch (method){
            case APPLETS:{
                jsapiBusiness.closeOrder(orderId);
            }
            default:{
                h5Business.closeOrder(orderId);
            }
        }
        return null;
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
    public <T> T queryOrder(String orderId, PayMethod method, Class<T> clazz) {
        switch (method){
            case APPLETS:{
                return (T) jsapiBusiness.queryOrderByOrderId(orderId);
            }
            default:{
                return (T) h5Business.queryOrderByOrderId(orderId);
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
    public <T> T refundOrder(PayParams param, Class<T> clazz) {
        return (T) refundBusiness.orderRefund(param);
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
    public <T> T queryRefundOrder(String orderId, String orderRefundId, Class<T> clazz) {
        return (T) refundBusiness.queryRefund(orderRefundId);
    }
}
