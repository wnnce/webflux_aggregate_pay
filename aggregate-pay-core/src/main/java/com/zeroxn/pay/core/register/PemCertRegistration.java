package com.zeroxn.pay.core.register;

/**
 * @Author: lisang
 * @DateTime: 2023-09-10 14:30:25
 * @Description: Pem证书注册实例
 */
public class PemCertRegistration {
    private final boolean isPublic;
    private final String[] certPaths;

    public PemCertRegistration(boolean isPublic, String ...certPaths) {
        this.isPublic = isPublic;
        this.certPaths = certPaths;
    }
    public boolean isPublic() {
        return isPublic;
    }

    public String[] getCertPaths() {
        return certPaths;
    }
}
