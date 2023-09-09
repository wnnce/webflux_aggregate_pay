package com.zeroxn.pay.core.register;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 19:20:52
 * @Description: 支付私钥/公钥证书注册器
 */
public class CertRegistry {
    private final List<CertRegistration> certRegistrationList = new ArrayList<>();

    public CertRegistry(){}

    public void add(String certPasswd, String ...certPath){
        certRegistrationList.add(new CertRegistration(certPasswd, certPath));
    }

    public List<CertRegistration> getCertRegistrationList() {
        return certRegistrationList;
    }
}
