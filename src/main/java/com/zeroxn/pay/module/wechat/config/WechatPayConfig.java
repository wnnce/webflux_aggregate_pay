package com.zeroxn.pay.module.wechat.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @Author: lisang
 * @DateTime: 2023/5/20 下午7:22
 * @Description: 微信支付配置类
 */
@ConfigurationProperties("pay.wechat")
@ConditionalOnProperty(prefix = "pay", name = "wechat.enable", havingValue = "true")
public class WechatPayConfig {
    /**
     * 是否启用微信支付
     */
    private Boolean enable;
    /**
     * 微信支付商家密钥
     */
    private String apiV3Key;
    /**
     * 微信支付商户号
     */
    private String merchantId;
    /**
     * 微信小程序ID
     */
    private String appId;
    /**
     * 微信支付币种类型 境内商户仅支持人民币 CNY
     */
    private String currency;
    /**
     * 签名验证方式
     */
    private String signType;
    /**
     * 微信支付成功通知地址
     */
    private String successNotifyUrl;
    /**
     * 微信支付退款成功通知地址
     */
    private String refundNotifyUrl;
    /**
     * 微信支付商户证书序列号
     */
    private String merchantSerialNumber;
    /**
     * 微信支付商户私钥
     */
    private String privateKey;

    public WechatPayConfig() {
    }

    @ConstructorBinding
    public WechatPayConfig(@NotNull Boolean enable, @NotNull String apiV3Key, @NotNull String merchantId, @NotNull String appId,
                           @DefaultValue("CNY") String currency, @DefaultValue("RSA") String signType, @NotNull String successNotifyUrl,
                           @NotNull String refundNotifyUrl, @NotNull String merchantSerialNumber, @NotNull String privateKey) {
        this.enable = enable;
        this.apiV3Key = apiV3Key;
        this.merchantId = merchantId;
        this.appId = appId;
        this.currency = currency;
        this.signType = signType;
        this.successNotifyUrl = successNotifyUrl;
        this.refundNotifyUrl = refundNotifyUrl;
        this.merchantSerialNumber = merchantSerialNumber;
        this.privateKey = privateKey;
    }

    public Boolean getEnable() {
        return enable;
    }

    public String getApiV3Key() {
        return apiV3Key;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getAppId() {
        return appId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getSignType() {
        return signType;
    }

    public String getSuccessNotifyUrl() {
        return successNotifyUrl;
    }

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }

    public String getMerchantSerialNumber() {
        return merchantSerialNumber;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
