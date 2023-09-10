package com.zeroxn.pay.core.register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 19:20:52
 * @Description: 支付私钥/公钥证书注册器
 */
public class CertRegistry {
    private final List<PfxCertRegistration> pfxCertList = new ArrayList<>();
    private final List<String> cerCertList = new ArrayList<>();
    private final List<PemCertRegistration> pemCertList = new ArrayList<>();

    public CertRegistry(){}

    public CertRegistry addPfxCert(String certPasswd, String ...certPaths){
        pfxCertList.add(new PfxCertRegistration(certPasswd, certPaths));
        return this;
    }

    public CertRegistry addCerCert(String ...certPaths) {
        cerCertList.addAll(Arrays.stream(certPaths).toList());
        return this;
    }

    public CertRegistry addPemCert(boolean isPublic, String ...certPaths) {
        pemCertList.add(new PemCertRegistration(isPublic, certPaths));
        return this;
    }

    public List<PfxCertRegistration> getPfxCertList() {
        return this.pfxCertList;
    }

    public List<String> getCerCertList() {
        return this.cerCertList;
    }

    public List<PemCertRegistration> getPemCertList() {
        return this.pemCertList;
    }
}
