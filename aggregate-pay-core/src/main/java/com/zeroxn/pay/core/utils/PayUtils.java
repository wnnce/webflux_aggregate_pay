package com.zeroxn.pay.core.utils;

import com.zeroxn.pay.core.entity.PayCert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 19:40:27
 * @Description: 核心包工具类
 */
public class PayUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.isEmpty();
    }

    /**
     * 读取pfx后缀的证书文件，读取其中的私钥、公钥、证书ID
     * @param certPath 证书文件路径 支持classpath:路径和绝对路径
     * @param certPassword 证书密码
     * @return 返回证书类
     * @throws Exception
     */
    public static PayCert readerCertByPfx(String certPath, String certPassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream inputStream = readerFile(certPath);
        keyStore.load(inputStream, certPassword.toCharArray());
        inputStream.close();
        String privateKeyAlias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(privateKeyAlias, certPassword.toCharArray());
        X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(privateKeyAlias);
        String certId = x509Certificate.getSerialNumber().toString(10);
        PublicKey publicKey = x509Certificate.getPublicKey();
        return new PayCert(certId, publicKey, privateKey);
    }

    /**
     * 读取cer后缀的证书文件，读取其中的公钥
     * @param certPath 证书文件路径
     * @return 返回证书类
     * @throws FileNotFoundException 文件不存在异常
     * @throws CertificateException 获取证书异常
     */
    public static PayCert readerCertByCer(String certPath) throws FileNotFoundException, CertificateException {
        FileInputStream inputStream = readerFile(certPath);
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) factory.generateCertificate(inputStream);
        PublicKey publicKey = certificate.getPublicKey();
        return new PayCert(publicKey);
    }

    /**
     * 读取指定路径的文件并转换为文件输入流
     * @param path 文件路径 支持classpath:路径和绝对路径
     * @return 返回文件的文件输入流
     * @throws FileNotFoundException 文件不存在异常
     */
    private static FileInputStream readerFile(String path) throws FileNotFoundException {
        if(path.startsWith("classpath:")){
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String filePath = path.substring(path.indexOf(":") + 1);
            return new FileInputStream(classLoader.getResource(filePath).getPath());
        }else {
            return new FileInputStream(path);
        }
    }
}
