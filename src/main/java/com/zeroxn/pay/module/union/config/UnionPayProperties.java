package com.zeroxn.pay.module.union.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 上午11:46
 * @Description: 云闪付配置类
 */
@ConfigurationProperties(prefix = "pay.union")
@ConditionalOnProperty(value = "pay.union", havingValue = "true")
public class UnionPayProperties {
    /**
     * 是否开启云闪付支付
     */
    private Boolean enable;
    /**
     * 编码方式 支持UTF-8和GBK 默认UTF-8
     */
    private String charset;
    /**
     * 用以签名的签名值
     */
    private String sign;
    /**
     * 签名方法 01：RSA、11：SHA-256、12：SM3 暂时只支持SHA-256 默认SHA-256
     */
    private String signType;
    /**
     * 云闪付系统内的商户ID
     */
    private String merchantId;
    /**
     * 云闪付系统内商户证书ID
     */
    private String certId;
    /**
     * 云闪付系统商户的私钥
     */
    private String privateKey;
    /**
     * 交易的币种 默认为人民币CNY：156
     */
    private String currency;
    /**
     * 支付成功后云闪付的异步通知地址
     */
    private String notifyUrl;

    public UnionPayProperties(){}

    @ConstructorBinding
    public UnionPayProperties(Boolean enable, @DefaultValue("UTF-8") String charset, @NotNull String sign,
                              @DefaultValue("11") String signType, @NotNull String merchantId, @NotNull String certId,
                              @NotNull String privateKey, @DefaultValue("156") String currency, @NotNull String notifyUrl){
        this.enable = enable;
        this.charset = charset;
        this.sign = sign;
        this.signType = signType;
        this.merchantId = merchantId;
        this.certId = certId;
        this.privateKey = privateKey;
        this.currency = currency;
        this.notifyUrl = notifyUrl;
    }

    public Boolean getEnable() {
        return enable;
    }

    public String getCharset() {
        return charset;
    }

    public String getSign() {
        return sign;
    }

    public String getSignType() {
        return signType;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getCertId() {
        return certId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getCurrency() {
        return currency;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }
}
