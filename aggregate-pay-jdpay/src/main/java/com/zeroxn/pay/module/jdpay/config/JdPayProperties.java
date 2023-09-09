package com.zeroxn.pay.module.jdpay.config;

import com.zeroxn.pay.module.jdpay.utils.JdPayUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 上午10:51
 * @Description: 京东支付配置类
 */
@ConfigurationProperties(prefix = "pay.jd")
public class JdPayProperties {
    /**
     * 京东支付版本号 默认 v2.0
     */
    private String version;
    /**
     * 交易信息签名
     */
    private String sign;
    /**
     * 京东支付内的商户号
     */
    private String merchantId;
    /**
     * 货币类型，默认：人民币CNY
     */
    private String currency;
    /**
     * 京东支付生成的DES密钥
     */
    private String desKey;
    /**
     * 京东支付公钥字符串
     */
    private String publicKey;
    /**
     * 京东支付私钥字符串
     */
    private String privateKey;
    /**
     * 支付完成后的异步通知地址
     */
    private String notifyUrl;

    /**
     * 从公钥字符串解析得来的公钥证书
     */
    private PublicKey pubKey;
    /**
     * 从私钥字符串解析得来的私钥证书
     */
    private PrivateKey priKey;
    public JdPayProperties(){}
    @ConstructorBinding
    public JdPayProperties(Boolean enable, @DefaultValue("v2.0") String version, @NotNull String sign, @NotNull String merchantId,
                           @DefaultValue("CNY") String currency, @NotNull String desKey, @NotNull String privateKey,
                           @NotNull String publicKey, @NotNull String notifyUrl) throws Exception{

        this.version = version;
        this.sign = sign;
        this.merchantId = merchantId;
        this.currency = currency;
        this.desKey = desKey;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.notifyUrl = notifyUrl;
        this.pubKey = JdPayUtils.readKeyFromString(this.publicKey, PublicKey.class);
        this.priKey = JdPayUtils.readKeyFromString(this.privateKey, PrivateKey.class);
    }

    public String getVersion() {
        return version;
    }

    public String getSign() {
        return sign;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDesKey() {
        return desKey;
    }

    public String getPublicKeyString() {
        return publicKey;
    }

    public String getPrivateKeyString() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return pubKey;
    }

    public PrivateKey getPrivateKey() {
        return priKey;
    }
    public String getNotifyUrl() {
        return notifyUrl;
    }
}
