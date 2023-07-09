package com.zeroxn.pay.module.jdpay.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 上午10:51
 * @Description: 京东支付配置类
 */
@ConfigurationProperties(prefix = "pay.jd")
@ConditionalOnProperty(value = "pay.jd.enable", havingValue = "true")
public class JdPayProperties {
    /**
     * 是否开启京东支付支持
     */
    private Boolean enable;
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
     * 支付完成后的异步通知地址
     */
    private String notifyUrl;
    public JdPayProperties(){}
    @ConstructorBinding
    public JdPayProperties(Boolean enable, @DefaultValue("v2.0") String version, @NotNull String sign, @NotNull String merchantId,
                           @DefaultValue("CNY") String currency, @NotNull String notifyUrl){

        this.enable = enable;
        this.version = version;
        this.sign = sign;
        this.merchantId = merchantId;
        this.currency = currency;
        this.notifyUrl = notifyUrl;
    }

    public Boolean getEnable() {
        return enable;
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

    public String getNotifyUrl() {
        return notifyUrl;
    }
}
