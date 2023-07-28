package com.zeroxn.pay.module.paypal.constant;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 下午3:48
 * @Description: Paypal支付模块常量类
 */
public class PaypalConstant {
    private static final String BASE_URL = "https://api-m.sandbox.paypal.com";
    public static final String AUTH_URL = BASE_URL + "/v1/oauth2/token";
    public static final String AUTH_FORM_KEY = "grant_type";
    public static final String AUTH_FORM_VALUE = "client_credentials";
    private static final String CREATE_ORDER_URL = BASE_URL + "v2/checkout/orders";
    private static final String QUERY_ORDER_URL = BASE_URL + "/v2/checkout/orders/{id}";
}
