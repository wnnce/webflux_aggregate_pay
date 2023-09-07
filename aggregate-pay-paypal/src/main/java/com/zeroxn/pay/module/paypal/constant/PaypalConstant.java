package com.zeroxn.pay.module.paypal.constant;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 下午3:48
 * @Description: Paypal支付模块常量类
 */
public class PaypalConstant {
    /**
     * PayPal支付基础URL
     */
    private static final String BASE_URL = "https://api-m.sandbox.paypal.com";
    /**
     * PayPal获取账户Token Url
     */
    public static final String AUTH_URL = BASE_URL + "/v1/oauth2/token";
    public static final String AUTH_FORM_KEY = "grant_type";
    public static final String AUTH_FORM_VALUE = "client_credentials";
    /**
     * PayPal创建订单URL
     */
    public static final String CREATE_ORDER_URL = BASE_URL + "/v2/checkout/orders";
    /**
     * PayPal查询订单URL
     */
    public static final String QUERY_ORDER_URL = BASE_URL + "/v2/checkout/orders/{id}";
    /**
     * PayPal收款URL
     */
    public static final String CAPTURE_ORDER_URL = BASE_URL + "/v2/checkout/orders/{id}/capture";
    /**
     * 订单退款URL
     */
    public static final String REFUND_ORDER_URL = BASE_URL + "/v2/payments/captures/{id}/refund";
    /**
     * PayPal查询退款订单URL
     */
    public static final String QUERY_REFUND_URL = BASE_URL + "/v2/payments/refunds/{id}";
}
