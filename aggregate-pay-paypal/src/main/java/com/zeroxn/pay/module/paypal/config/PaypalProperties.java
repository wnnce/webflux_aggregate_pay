package com.zeroxn.pay.module.paypal.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午12:39
 * @Description: Paypal支付参数类
 */
@ConfigurationProperties("pay.paypal")
public class PaypalProperties {
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

    private String token = "A21AAJgxrcGnpSgI-peNk8dY61rHeD6vHFGJeYjkkfAEeuBRMPIUt1m51WbOr4cDnqBGRigY0otwPyzaYROvNPBEC8lE8Ufsg";

    @ConstructorBinding
    public PaypalProperties(@NotNull String clientId,@NotNull String secret, @DefaultValue("CNY") String currency, String notifyUrl) {
        this.clientId = clientId;
        this.secret = secret;
        this.currency = currency;
        this.notifyUrl = notifyUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSecret() {
        return secret;
    }

    public String getCurrency() {
        return currency;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }
}
