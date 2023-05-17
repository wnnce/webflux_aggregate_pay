package com.zeroxn.pay.module.alipay.constant;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 13:25
 * @Description: 支付宝支付、登录公共常量
 */
@Slf4j
public class AlipayConstant {
    /**
     * 支付宝应用 appId
     */
    private static final String APPID = "";
    /**
     * 开发者私钥
     */
    private static final String PRIVATE_KEY = "";
    /**
     * 支付宝公钥
     */
    private static final String ALIPAY_PUBLIC_KEY = "";
    /**
     * 签名算法类型 仅支持RSA2
     */
    private static final String SIGN_TYPE = "RSA2";

    /**
     * 编码集 支持 GBK 和 UTF-8
     */
    private static final String CHARSET = "UTF-8";
    /**
     * 微信支付买家userId
     */
    private static final String SELLER_ID = "";
    /**
     * 支付成功支付宝异步回调通知地址
     */
    private static final String SUCCESS_NOTIFY_URL = "";

    /**
     * 支付宝请求API配置
     */
    private static final AlipayConfig CONFIG;
    private static final AlipayClient alipayClient;
    static  {
        CONFIG = new AlipayConfig();
        // 支付宝网关 固定地址
        CONFIG.setServerUrl("https://openapi.alipay.com/gateway.do");
        CONFIG.setAppId(APPID);
        CONFIG.setPrivateKey(PRIVATE_KEY);
        // 参数返回格式 仅支持json
        CONFIG.setFormat("json");
        CONFIG.setCharset(CHARSET);
        CONFIG.setAlipayPublicKey(ALIPAY_PUBLIC_KEY);
        CONFIG.setSignType(SIGN_TYPE);
        try{
            alipayClient = new DefaultAlipayClient(CONFIG);
        } catch (AlipayApiException e) {
            log.error("alipayClient初始化失败");
            throw new RuntimeException(e);
        }
    }

    public static AlipayClient getAlipayClient() {
        return alipayClient;
    }

    public static String getAPPID() {
        return APPID;
    }

    public static String getAlipayPublicKey() {
        return ALIPAY_PUBLIC_KEY;
    }

    public static String getSignType() {
        return SIGN_TYPE;
    }

    public static String getCharset() {
        return CHARSET;
    }

    public static String getSuccessNotifyUrl() {
        return SUCCESS_NOTIFY_URL;
    }


    public static String getSellerId() {
        return SELLER_ID;
    }
}
