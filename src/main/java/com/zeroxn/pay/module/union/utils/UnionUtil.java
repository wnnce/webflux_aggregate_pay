package com.zeroxn.pay.module.union.utils;

import com.zeroxn.pay.core.exception.PaySystemException;
import com.zeroxn.pay.module.union.config.UnionPayCertManager;
import com.zeroxn.pay.module.union.constant.UnionConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * @Author: lisang
 * @DateTime: 2023/6/2 上午9:07
 * @Description:
 */
@Component
@ConditionalOnClass(UnionPayCertManager.class)
public class UnionUtil {
    private static final Logger logger = LoggerFactory.getLogger(UnionUtil.class);
    private static UnionPayCertManager unionPayCertManager;
    @Autowired
    public void setUnionPayCertManager(UnionPayCertManager unionPayCertManager){
        UnionUtil.unionPayCertManager = unionPayCertManager;
    }
    /**
     * 过滤Map中的空Value键值对
     * @param data 入参Map
     * @return 返回过滤空键值对后的Map
     */
    public static Map<String, String> filterEmpty(Map<String, String> data){
        Set<String> keySet = data.keySet();
        Map<String, String> newMap = new HashMap<>();
        keySet.forEach(key -> {
            String value = data.get(key);
            if(!isEmpty(value)){
                newMap.put(key, value);
            }
        });
        return newMap;
    }

    /**
     * 对请求数据进行签名
     * @param data 请求数据
     * @param certPath 签名证书路径
     * @param charset 编码格式
     * @return 返回加了签名后的请求数据
     */
    public static Map<String, String> sign(Map<String, String> data, String certPath, String charset){
        Map<String, String> filterMap = filterEmpty(data);
        try{
            String signMethod = filterMap.get("signMethod");
            if("01".equals(signMethod)){
                return signByCert(filterMap, certPath, charset);
            } else if ("11".equals(signMethod)){
                return signBySignKey(filterMap, certPath, charset);
            } else {
                UnionUtil.logger.error("不支持的签名方式，签名方式：{}", signMethod);
                throw new PaySystemException("云闪付不支持的签名方式");
            }
        }catch (Exception ex){
            UnionUtil.logger.error("请求数据签名失败，错误消息：{}", ex.getMessage());
            throw new PaySystemException("云闪付参数签名失败");
        }
    }

    // 字符串判空
    public static boolean isEmpty(String value){
        return value == null || value.length() == 0;
    }

    /**
     * sha256摘要加密
     * @param data 请求数据
     * @param signKey 摘要加密的密钥
     * @param charset 编码格式
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private static Map<String, String> signBySignKey(Map<String, String> data, String signKey, String charset) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String reqStr = mapToString(data, charset, true, false);
        String value = reqStr + "&" + bytesToHexString(sha256(signKey.getBytes(charset)));
        data.put("signature", bytesToHexString(sha256(value.getBytes(charset))));
        return data;
    }

    /**
     * RSA加密
     * @param data 请求数据
     * @param certPath 商户私钥证书路径
     * @param charset 编码格式
     * @return
     * @throws Exception
     */
    private static Map<String, String> signByCert(Map<String, String> data, String certPath, String charset) throws Exception {
        String certId = UnionUtil.unionPayCertManager.getCertId(certPath);
        data.put("certId", certId);
        String reqStr = mapToString(data, charset, true, false);
        byte[] bytes = sha256(reqStr.getBytes(charset));
        String sha256Hex = bytesToHexString(bytes).toLowerCase();
        String signature = Base64.getEncoder().encodeToString(signatureSHA256(UnionUtil.unionPayCertManager.getPrivateKey(certPath), sha256Hex.getBytes()));
        data.put("signature", signature);
        return data;
    }
    /**
     * 将Map集合中的数据转换为key=value&key=value的形式 并对中文进行URL编码
     * @param data Map集合
     * @param charset 编码格式默认 UTF-8
     * @return 返回经过URL编码后的数据字符串
     */
    public static String mapToString(Map<String, String> data, String charset, boolean isSort, boolean isEncode) {
        try{
            List<String> keyList = new ArrayList<>(data.keySet());
            StringBuilder builder = new StringBuilder();
            if(isSort)
                Collections.sort(keyList);
            for (String key : keyList) {
                String value = data.get(key);
                if(isEncode && !UnionUtil.isEmpty(value)){
                    value = URLEncoder.encode(value, charset);
                }
                builder.append(key).append("=").append(value).append("&");
            }
            return builder.substring(0, builder.length() - 1);
        }catch (UnsupportedEncodingException ex){
            UnionUtil.logger.error("Map集合转字符串失败，错误消息：{}", ex.getMessage());
            throw new PaySystemException("云闪付返回参数转换失败");
        }
    }
    public static Map<String, String> stringToMap(String str, String split, String separate){
        Map<String, String> map = new HashMap<>();
        String[] strs = str.split(split);
        for (String s : strs) {
            int index = s.indexOf(separate);
            String key = s.substring(0, index);
            String value = s.substring(index + 1);
            map.put(key, value);
        }
        return map;
    }
    private static byte[] signatureSHA256(PrivateKey privateKey, byte[] bytes) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(bytes);
        return signature.sign();
    }
    private static byte[] sha256(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(bytes);
    }

    private static String bytesToHexString(byte[] bytes){
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /**
     * 读取pfx后缀的证书文件，读取其中的私钥、公钥、证书ID
     * @param certPath 证书文件路径 支持classpath:路径和绝对路径
     * @param certPassword 证书密码
     * @return 返回证书类
     * @throws Exception
     */
    public static UnionCert readerCertByPfx(String certPath, String certPassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(UnionConstant.SIGNCERTTYPE);
        FileInputStream inputStream = readerFile(certPath);
        keyStore.load(inputStream, certPassword.toCharArray());
        inputStream.close();
        String privateKeyAlias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(privateKeyAlias, certPassword.toCharArray());
        X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(privateKeyAlias);
        String certId = x509Certificate.getSerialNumber().toString(10);
        PublicKey publicKey = x509Certificate.getPublicKey();
        return new UnionCert(certId, privateKey, publicKey);
    }

    /**
     * 读取cer后缀的证书文件，读取其中的公钥
     * @param certPath 证书文件路径
     * @return 返回证书类
     * @throws FileNotFoundException 文件不存在异常
     * @throws CertificateException 获取证书异常
     */
    public static UnionCert readerCertByCer(String certPath) throws FileNotFoundException, CertificateException {
        FileInputStream inputStream = readerFile(certPath);
        CertificateFactory factory = CertificateFactory.getInstance(UnionConstant.CERTTYPE);
        X509Certificate certificate = (X509Certificate) factory.generateCertificate(inputStream);
        PublicKey publicKey = certificate.getPublicKey();
        return new UnionCert(publicKey);
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
    public static <K, V> MultiValueMap<K, V> mapToMultiValueMap(Map<K, V> sourceMap){
        MultiValueMap<K, V> multiValueMap = new LinkedMultiValueMap<>();
        sourceMap.forEach(multiValueMap::add);
        return multiValueMap;
    }
}
