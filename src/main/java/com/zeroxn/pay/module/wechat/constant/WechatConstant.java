package com.zeroxn.pay.module.wechat.constant;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 11:51
 * @Description: 微信支付所需要的公共常量
 */
public class WechatConstant {

    /**
     * 微信支付商家密钥
     */
    private static final String API_V3_KEY = "";

    /**
     * 微信支付商户号
     */
    private static final String MERCHANT_ID = "";

    /**
     * 微信小程序id
     */
    private static final String APP_ID = "";

    /**
     * 支付成功通知地址，用于调用微信支付成功后的微信支付系统发起异步回调通知 网址必须使用 https
     */
    private static final String SUCCESS_NOTIFY_URL = "";

    /**
     * 退款成功通知地址 格式要求和支付成功通知地址一致
     */
    private static final String REFUND_NOTIFY_URL = "";

    /**
     * 商户证书序列号
     */
    private static final String merchantSerialNumber = "";

    /**
     * 商户API私钥内容
     */
    private static String privateKeyValue;

    private static final Config CONFIG;

    static {
        /*try {
            // 微信商户私钥文件 放在SpringBoot项目 resource 目录下 如果不想使用文件读取 可以直接将私钥内容赋值给 privateKeyValue
            ClassPathResource resource = new ClassPathResource("/cert/apiclient_key.pem");
            InputStream inputStream = resource.getInputStream();
            int count = inputStream.available();
            byte[] content = new byte[count];
            inputStream.read(content);
            privateKeyValue = new String(content);
            inputStream.close();
        } catch (FileNotFoundException e) {
            log.error("商户私钥文件不存在");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        // 初始化商户配置
        CONFIG =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(MERCHANT_ID)
                        .privateKey(privateKeyValue)
                        .merchantSerialNumber(merchantSerialNumber)
                        .apiV3Key(API_V3_KEY)
                        .build();
    }
    public static Config getConfig() {
        return CONFIG;
    }
    public static String getAppId() {
        return APP_ID;
    }
    public static String getMerchantId() {
        return MERCHANT_ID;
    }
    public static String getSuccessNotifyUrl() {
        return SUCCESS_NOTIFY_URL;
    }
    public static String getRefundNotifyUrl() {
        return REFUND_NOTIFY_URL;
    }
}
