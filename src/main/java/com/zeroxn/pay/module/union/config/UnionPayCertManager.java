package com.zeroxn.pay.module.union.config;

import com.zeroxn.pay.module.union.exception.UnionPayException;
import com.zeroxn.pay.module.union.utils.UnionCert;
import com.zeroxn.pay.module.union.utils.UnionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/6/6 上午11:55
 * @Description: 云闪付证书管理类
 */
public class UnionPayCertManager {
    private static final Logger logger = LoggerFactory.getLogger(UnionPayCertManager.class);
    private final Map<String, UnionCert> certMap = new HashMap<>();
    public UnionPayCertManager(UnionPayProperties properties) throws Exception{
        this.init(properties.getSignCertPwd(), properties.getSignCertPath(), properties.getEncryptCertPath(),
                properties.getMiddleCertPath(), properties.getRootCertPath());
    }
    private void init(String certPassword, String ...certPath) throws Exception{
        for (String path : certPath){
            if(UnionUtil.isEmpty(path)){
                continue;
            }
            UnionCert unionCert = null;
            if (path.endsWith(".pfx")){
                unionCert = UnionUtil.readerCertByPfx(path, certPassword);
            } else if (path.endsWith(".cer")) {
                unionCert = UnionUtil.readerCertByCer(path);
            }else {
                UnionPayCertManager.logger.error("证书路径格式错误，错误路径：{}", path);
                throw new UnionPayException("云闪付初始化失败");
            }
            this.certMap.put(path, unionCert);
        }
    }
    public PrivateKey getPrivateKey(String certPath){
        UnionCert unionCert = certMap.get(certPath);
        return unionCert.getPrivateKey();
    }
    public PublicKey getPublicKey(String certPath){
        UnionCert unionCert = certMap.get(certPath);
        return unionCert.getPublicKey();
    }
    public String getCertId(String certPath){
        UnionCert unionCert = certMap.get(certPath);
        return unionCert.getCertId();
    }
}