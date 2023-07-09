package com.zeroxn.pay.module.paypal.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午12:39
 * @Description: Paypal支付参数类
 */
@ConfigurationProperties("pay.paypal")
@ConditionalOnProperty(value = "pay.paypal.enable", havingValue = "true")
public class PaypalProperties {
    private final Boolean enable;
    /**
     * 商户账号ID
     */
    private final String clientId;
    /**
     * 商户账号密钥
     */
    private final String secret;
    /**
     * 币种 默认：人民币 CNY
     */
    private final String currency;
    /**
     * 支付成功后的通知地址
     */
    private final String notifyUrl;

    @ConstructorBinding
    public PaypalProperties(Boolean enable, String clientId, String secret, @DefaultValue("CNY") String currency, String notifyUrl) {
        this.enable = enable;
        this.clientId = clientId;
        this.secret = secret;
        this.currency = currency;
        this.notifyUrl = notifyUrl;
    }
}
