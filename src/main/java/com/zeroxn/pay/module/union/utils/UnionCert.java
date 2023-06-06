package com.zeroxn.pay.module.union.utils;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author: lisang
 * @DateTime: 2023/6/6 下午12:43
 * @Description:
 */
public class UnionCert {
    private String certId;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public UnionCert() {}

    public UnionCert(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public UnionCert(String certId, PrivateKey privateKey, PublicKey publicKey) {
        this.certId = certId;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getCertId() {
        return certId;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
