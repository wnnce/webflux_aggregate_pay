package com.zeroxn.pay.core.entity;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 19:32:02
 * @Description: 支付证书实体类
 */
public class PayCert {
    private String certId;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public PayCert(){}

    public PayCert(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PayCert(String certId, PublicKey publicKey, PrivateKey privateKey) {
        this.certId = certId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getCertId() {
        return certId;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
