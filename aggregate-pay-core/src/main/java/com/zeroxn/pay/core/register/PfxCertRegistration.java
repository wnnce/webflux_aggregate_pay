package com.zeroxn.pay.core.register;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 19:20:29
 * @Description: Pfx私钥/公钥证书注册实例
 */
public class PfxCertRegistration {
    private final String certPassword;
    private final String[] certPaths;

    public PfxCertRegistration(String certPassword, String ...certPath){
        this.certPassword = certPassword;
        this.certPaths = certPath;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public String[] getCertPaths() {
        return certPaths;
    }
}
