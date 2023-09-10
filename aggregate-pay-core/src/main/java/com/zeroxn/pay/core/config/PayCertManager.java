package com.zeroxn.pay.core.config;

import com.zeroxn.pay.core.entity.PayCert;
import com.zeroxn.pay.core.register.CertRegistry;
import com.zeroxn.pay.core.register.PemCertRegistration;
import com.zeroxn.pay.core.register.PfxCertRegistration;
import com.zeroxn.pay.core.utils.PayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 19:35:39
 * @Description: 支付证书管理类，在项目启动时通过传递的路径拿到所有的证书并放到Map中进行缓存，子模块可以通过证书路径来获取证书
 */
public class PayCertManager {
    private static final Logger logger = LoggerFactory.getLogger(PayCertManager.class);
    // 使用ConcurrentHashMap ，线程安全
    private final Map<String, PayCert> certMap = new ConcurrentHashMap<>();
    public PayCertManager(CertRegistry registry) {
        CompletableFuture<Void> com1 = CompletableFuture.runAsync(() -> {
            if (!registry.getPfxCertList().isEmpty()) {
                try {
                    initPfxCert(registry.getPfxCertList());
                } catch (Exception e) {
                    logger.error("初始化Pfx证书失败，错误消息：{}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });
        CompletableFuture<Void> com2 = CompletableFuture.runAsync(() -> {
            if (!registry.getCerCertList().isEmpty()) {
                try {
                    initCerCert(registry.getCerCertList());
                } catch (Exception e) {
                    logger.error("初始化Cer证书失败，错误消息：{}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });
        CompletableFuture<Void> com3 = CompletableFuture.runAsync(() -> {
            if (!registry.getPemCertList().isEmpty()) {
                try {
                    initPemCert(registry.getPemCertList());
                } catch (Exception e) {
                    logger.error("初始化Pem证书失败，错误消息：{}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });
        CompletableFuture.allOf(com1, com2, com3).join();
    }

    private void initPfxCert(List<PfxCertRegistration> pfxCertList) throws Exception {
        logger.info("开始初始化Pfx证书，注册实例数量：{}", pfxCertList.size());
        for (PfxCertRegistration registration : pfxCertList){
            for (String path : registration.getCertPaths()){
                if(PayUtils.isEmpty(path) || !path.endsWith("pfx")){
                    logger.warn("初始化Pfx证书，证书后缀错误，证书路径：{}", path);
                    continue;
                }
                PayCert pfxCert = PayUtils.readerCertByPfx(path, registration.getCertPassword());
                logger.info("Pfx证书获取成功，证书名称：{}", path.substring(path.lastIndexOf(File.separator) + 1));
                this.certMap.put(path, pfxCert);
            }
        }
    }
    private void initCerCert(List<String> cerCertList) throws Exception {
        logger.info("开始初始化Cer证书，证书数量：{}", cerCertList.size());
        for (String path : cerCertList) {
            if (PayUtils.isEmpty(path) || !path.endsWith(".cer")) {
                logger.warn("初始化cer证书，证书后缀错误，证书路径：{}", path);
                continue;
            }
            PayCert cerCert = PayUtils.readerCertByCer(path);
            logger.info("cer证书获取成功，证书名称：{}", path.substring(path.lastIndexOf(File.separator) + 1));
            this.certMap.put(path, cerCert);
        }
    }

    private void initPemCert(List<PemCertRegistration> pemCertList) throws Exception {
        logger.info("开始初始化Pem证书，证书数量：{}", pemCertList.size());
        for (PemCertRegistration registration : pemCertList){
            for (String path : registration.getCertPaths()){
                if (PayUtils.isEmpty(path) || !path.endsWith(".pem")){
                    logger.warn("初始化pem证书，证书后缀错误，证书路径：{}", path);
                    continue;
                }
                PayCert pemCert = PayUtils.readerCertByPem(path, registration.isPublic());
                logger.info("证书获取成功，证书名称：{}", path.substring(path.lastIndexOf(File.separator) + 1));
                this.certMap.put(path, pemCert);
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
