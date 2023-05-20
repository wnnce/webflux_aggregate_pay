package com.zeroxn.pay.core.adapter;

import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.exception.PayException;
import com.zeroxn.pay.module.alipay.AlipayPayTemplate;
import com.zeroxn.pay.module.wechat.WechatPayHandler;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 09:40
 * @Description: APi适配器 统一提供对外调用的接口
 */

public class ApiAdapter {
    private final AlipayPayTemplate alipayPayHandler;
    private final WechatPayHandler wechatPayHandler;
    public ApiAdapter(AlipayPayTemplate alipayPayHandler, WechatPayHandler wechatPayHandler){
        this.alipayPayHandler = alipayPayHandler;
        this.wechatPayHandler = wechatPayHandler;
    }
    /**
     * 统一下单接口 可以提供的泛型
     * AlipayTradeCreateResponse.class 支付宝小程序支付
     * AlipayTradeWapPayResponse.class 支付宝WAP手机网站支付
     * AlipayTradePagePayResponse.class 支付宝电脑端网站下单
     * PrepayResponse.class 微信H5下单
     * PrepayWithRequestPaymentResponse.class 微信小程序下单
     *
     * @param param 下单参数 通过 PayParam.BilderXXX 构建
     * @param platform 支付平台 枚举类
     * @param method 支付方式 枚举类
     * @param clazz 泛型
     * @return 返回支付平台的响应体
     * @param <T>
     */
    public <T> T confirmOrder(PayParams param, PayPlatform platform, PayMethod method, Class<T> clazz){
        switch (platform){
            case ALIPAY: return alipayPayHandler.handlerConfirmOrder(param, method, clazz);
            case WECHAT: return wechatPayHandler.handlerConfirmOrder(param, method, clazz);
            default: throw new PayException("不受支持的平台");
        }
    }

    /**
     * 统一查询订单接口 可以提供的泛型
     * AlipayTradeQueryResponse.class 查询支付宝订单
     * Transaction.class 查询微信订单
     *
     * @param orderId 订单id订单详细信息或空
     * @param platform 支付平台
     * @param method 支付方式
     * @param clazz 泛型
     * @return
     * @param <T>
     */
    public <T> T queryOrder(String orderId, PayPlatform platform, PayMethod method, Class<T> clazz){
        switch (platform){
            case ALIPAY: return alipayPayHandler.handlerQueryOrder(orderId, method, clazz);
            case WECHAT: return wechatPayHandler.handlerQueryOrder(orderId, method, clazz);
            default: throw new PayException("不受支持的平台");
        }
    }

    /**
     * 关闭订单接口
     * @param orderId 订单id
     * @param platform 支付平台
     * @param method 支付方式
     */
    public <T> T closeOrder(String orderId, PayPlatform platform, PayMethod method, Class<T> clazz){
        switch (platform){
            case ALIPAY: return alipayPayHandler.handlerCloseOrder(orderId, method, clazz);
            case WECHAT: return wechatPayHandler.handlerCloseOrder(orderId, method, clazz);
            default: throw new PayException("不受支持的平台");
        }
    }

    /**
     * 订单退款接口 可以提供的泛型
     * Refund.class 微信支付退款
     * AlipayTradeRefundResponse.class 支付宝退款
     *
     * @param param 订单退款参数
     * @param platform 支付平台
     * @param clazz 泛型
     * @return 退款信息或空
     * @param <T>
     */
    public <T> T orderRefund(PayParams param, PayPlatform platform, Class<T> clazz){
        switch (platform){
            case ALIPAY: return alipayPayHandler.handlerOrderRefund(param, clazz);
            case WECHAT: return wechatPayHandler.handlerOrderRefund(param, clazz);
            default: throw new PayException("不受支持的平台");
        }
    }

    /**
     * 查询退款订单 可以提供的泛型
     * Refund.class 查询微信支付退款订单
     * AlipayTradeFastpayRefundQueryResponse.class 查询支付宝支付退款订单
     *
     * @param orderId 订单id
     * @param orderRefundId 订单退款id
     * @param platform 支付平台
     * @param clazz 泛型
     * @return 返回退款订单详情或空
     * @param <T>
     */
    public <T> T queryOrderRefund(String orderId, String orderRefundId, PayPlatform platform, Class<T> clazz){
        switch (platform){
            case ALIPAY: return alipayPayHandler.handlerQueryRefund(orderId, orderRefundId, clazz);
            case WECHAT: return wechatPayHandler.handlerQueryRefund(orderId, orderRefundId, clazz);
            default: throw new PayException("不受支持的平台");
        }
    }
}
