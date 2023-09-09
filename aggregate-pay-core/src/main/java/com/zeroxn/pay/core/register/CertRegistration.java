package com.zeroxn.pay.core.register;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 19:20:29
 * @Description: 私钥/公钥证书注册实例
 */
public class CertRegistration {
    private final String certPassword;
    private final String[] certPath;

    public CertRegistration(String certPassword, String ...certPath){
        this.certPassword = certPassword;
        this.certPath = certPath;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public String[] getCertPath() {
        return certPath;
    }
}
