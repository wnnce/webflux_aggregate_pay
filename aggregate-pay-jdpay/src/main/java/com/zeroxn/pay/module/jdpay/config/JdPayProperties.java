package com.zeroxn.pay.module.jdpay.config;

import com.zeroxn.pay.module.jdpay.utils.JdPayUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.crypto.SecretKey;
import javax.crypto.spec.DESedeKeySpec;
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
     * 京东支付公钥证书路径 支持classpath:路径
     */
    private String publicKeyPath;
    /**
     * 京东支付私钥证书路径 支持classpath:路径
     */
    private String privateKeyPath;
    /**
     * 支付成功后的异步通知地址
     */
    private String successNotifyUrl;

    /**
     * 退款成功的异步通知地址
     */
    private String refundNotifyUrl;

    /**
     * 从DES密钥字符串解析生产的密钥文件
     */
    private SecretKey secretKey;
    public JdPayProperties(){}
    @ConstructorBinding
    public JdPayProperties(@DefaultValue("v2.0") String version, @NotNull String merchantId, @DefaultValue("CNY") String currency,
                           @NotNull String desKey, @NotNull String privateKeyPath, @NotNull String publicKeyPath,
                           @NotNull String successNotifyUrl, String refundNotifyUrl) throws Exception{

        this.version = version;
        this.merchantId = merchantId;
        this.currency = currency;
        this.desKey = desKey;
        this.publicKeyPath = publicKeyPath;
        this.privateKeyPath = privateKeyPath;
        this.successNotifyUrl = successNotifyUrl;
        this.refundNotifyUrl = refundNotifyUrl;
        this.secretKey = JdPayUtils.initSecretKey(this.desKey);
    }

    public String getVersion() {
        return version;
    }


    public String getMerchantId() {
        return merchantId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDesKeyString() {
        return desKey;
    }

    public String getPublicKeyPath() {
        return publicKeyPath;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public String getSuccessNotifyUrl() {
        return successNotifyUrl;
    }

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }
}
