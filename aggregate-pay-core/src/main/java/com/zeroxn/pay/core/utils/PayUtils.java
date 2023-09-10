package com.zeroxn.pay.core.utils;

import com.zeroxn.pay.core.entity.PayCert;
import io.netty.handler.codec.base64.Base64Decoder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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
        FileInputStream inputStream = new FileInputStream(readerFile(certPath));
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
    public static PayCert readerCertByCer(String certPath) throws Exception {
        FileInputStream inputStream = new FileInputStream(readerFile(certPath));
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) factory.generateCertificate(inputStream);
        inputStream.close();
        PublicKey publicKey = certificate.getPublicKey();
        return new PayCert(publicKey);
    }

    public static PayCert readerCertByPem(String certPath, boolean isPublic) throws Exception {
        File certFile = readerFile(certPath);
        PemReader pemReader = new PemReader(new FileReader(certFile));
        PemObject pemObject = pemReader.readPemObject();
        pemReader.close();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PayCert payCert = null;
        if (isPublic){
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pemObject.getContent());
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            payCert = new PayCert(publicKey);
        }else {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            payCert = new PayCert(privateKey);
        }
        return payCert;
    }

    /**
     * 读取指定路径的文件并转换为文件输入流
     * @param path 文件路径 支持classpath:路径和绝对路径
     * @return 返回文件的文件输入流
     * @throws FileNotFoundException 文件不存在异常
     */
    private static File readerFile(String path) throws FileNotFoundException {
        if(path.startsWith("classpath:")){
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String filePath = path.substring(path.indexOf(":") + 1);
            return new File(classLoader.getResource(filePath).getPath());
        }else {
            return new File(path);
        }
    }
}
