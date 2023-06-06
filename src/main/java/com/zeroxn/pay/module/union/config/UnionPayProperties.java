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
@ConditionalOnProperty(value = "pay.union.enable", havingValue = "true")
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
     * 签名方法 01：RSA、11：SHA-256、12：SM3  默认RSA
     */
    private String signType;
    /**
     * 签名证书路径 支持 classpath:路径和绝对路径
     */
    private String signCertPath;
    /**
     * 签名证书密码
     */
    private String signCertPwd;
    /**
     * 签名证书类型 默认PKCS12
     */
    private String signCertType;
    /**
     * 敏感加密证书路径
     */
    private String encryptCertPath;
    /**
     * 中级证书路径
     */
    private String middleCertPath;
    /**
     * 根证书路径
     */
    private String rootCertPath;
    /**
     * 云闪付系统内的商户ID
     */
    private String merchantId;
    /**
     * 交易的币种 默认为人民币CNY：156
     */
    private String currency;
    /**
     * 支付成功后云闪付的异步通知地址
     */
    private String successNotifyUrl;
    /**
     * 退款成功后云闪付的异步通知地址
     */
    private String refundNotifyUrl;

    public UnionPayProperties(){}

    @ConstructorBinding
    public UnionPayProperties(Boolean enable, @DefaultValue("UTF-8") String charset, @DefaultValue("01") String signType,
                              @NotNull String signCertPath, @NotNull String signCertPwd, @DefaultValue("PKCS12") String signCertType,
                              String encryptCertPath, String middleCertPath, String rootCertPath, @NotNull String merchantId,
                              @DefaultValue("156") String currency, @NotNull String successNotifyUrl, @NotNull String refundNotifyUrl) throws Exception{
        this.enable = enable;
        this.charset = charset;
        this.signType = signType;
        this.signCertPath = signCertPath;
        this.signCertPwd = signCertPwd;
        this.signCertType = signCertType;
        this.encryptCertPath = encryptCertPath;
        this.middleCertPath = middleCertPath;
        this.rootCertPath = rootCertPath;
        this.merchantId = merchantId;
        this.currency = currency;
        this.successNotifyUrl = successNotifyUrl;
        this.refundNotifyUrl = refundNotifyUrl;
    }

    public Boolean getEnable() {
        return enable;
    }

    public String getCharset() {
        return charset;
    }

    public String getSignType() {
        return signType;
    }

    public String getSignCertPath() {
        return signCertPath;
    }

    public String getSignCertPwd() {
        return signCertPwd;
    }

    public String getSignCertType() {
        return signCertType;
    }

    public String getEncryptCertPath() {
        return encryptCertPath;
    }

    public String getMiddleCertPath() {
        return middleCertPath;
    }

    public String getRootCertPath() {
        return rootCertPath;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getSuccessNotifyUrl() {
        return successNotifyUrl;
    }

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }
}
