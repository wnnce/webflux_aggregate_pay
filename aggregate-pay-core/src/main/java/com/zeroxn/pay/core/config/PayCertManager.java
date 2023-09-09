package com.zeroxn.pay.core.config;

import com.zeroxn.pay.core.entity.PayCert;
import com.zeroxn.pay.core.register.CertRegistration;
import com.zeroxn.pay.core.utils.PayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 19:35:39
 * @Description: 支付证书管理类，在项目启动时通过传递的路径拿到所有的证书并放到Map中进行缓存，子模块可以通过证书路径来获取证书
 */
public class PayCertManager {
    private static final Logger logger = LoggerFactory.getLogger(PayCertManager.class);
    private final Map<String, PayCert> certMap = new HashMap<>();

    public PayCertManager(List<CertRegistration> certRegistrationList) throws Exception {
        logger.debug("证书注册实例数量：{}", certRegistrationList.size());
        for (CertRegistration registration : certRegistrationList){
            for (String path : registration.getCertPath()){
                if(PayUtils.isEmpty(path)){
                    continue;
                }
                PayCert unionCert = null;
                if (path.endsWith(".pfx")){
                    unionCert = PayUtils.readerCertByPfx(path, registration.getCertPassword());
                } else if (path.endsWith(".cer")) {
                    unionCert = PayUtils.readerCertByCer(path);
                }else {
                    logger.error("证书路径格式错误，错误路径：{}", path);
                }
                logger.debug("证书获取成功，证书路径：{}", path);
                this.certMap.put(path, unionCert);
            }
        }
    }

    public PublicKey getPublicKey(String certPath) {
        PayCert payCert = certMap.get(certPath);
        return payCert.getPublicKey();
    }

    public PrivateKey getPrivateKey(String certPath) {
        PayCert payCert = certMap.get(certPath);
        return payCert.getPrivateKey();
    }

    public String getCertId(String certPath) {
        PayCert payCert = certMap.get(certPath);
        return payCert.getCertId();
    }
}
