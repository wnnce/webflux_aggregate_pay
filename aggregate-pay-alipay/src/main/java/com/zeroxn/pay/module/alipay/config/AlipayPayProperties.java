package com.zeroxn.pay.module.alipay.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @Author: lisang
 * @DateTime: 2023/5/18 下午8:09
 * @Description: 支付宝支付配置类 其他bean都依赖这个bean pay.enable.true属性为true才会自动注入 否则其他bean都不会注入
 */
@ConfigurationProperties(prefix = "pay.alipay")
public class AlipayPayProperties {
    /**
     * 支付宝appid
     */
    private String appId;
    /**
     * 支付宝商户私钥
     */
    private String privateKey;
    /**
     * 支付宝公钥
     */
    private String publicKey;
    /**
     * 请求格式 目前仅支持JSON
     */
    private String format;
    /**
     * 签名验证方式
     */
    private String signType;
    /**
     * 编码格式
     */
    private String charSet;
    /**
     * 支付宝服务地址
     */
    private String serverUrl;
    /**
     * 支付宝卖家id 小程序支付需要
     */
    private String sellerId;
    /**
     * 支付宝支付成功通知地址
     */
    private String notifyUrl;
    public AlipayPayProperties(){}
    @ConstructorBinding
    public AlipayPayProperties(@NotNull String appId, @NotNull String privateKey, @NotNull String publicKey,
                               @DefaultValue("JSON") String format, @DefaultValue("RSA2") String signType, @DefaultValue("UTF-8") String charSet,
                               @DefaultValue("https://openapi-sandbox.dl.alipaydev.com/gateway.do") String serverUrl,
                               String sellerId, String notifyUrl) {
        this.appId = appId;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.format = format;
        this.signType = signType;
        this.charSet = charSet;
        this.serverUrl = serverUrl;
        this.sellerId = sellerId;
        this.notifyUrl = notifyUrl;
    }


    public String getAppId() {
        return appId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getFormat() {
        return format;
    }

    public String getSignType() {
        return signType;
    }

    public String getCharSet() {
        return charSet;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }
}
