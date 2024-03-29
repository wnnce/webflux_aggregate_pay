package com.zeroxn.pay.module.union.utils;

import com.zeroxn.pay.core.config.PayCertManager;
import com.zeroxn.pay.core.exception.PaySystemException;
import com.zeroxn.pay.module.union.config.ConditionalOnUnion;
import com.zeroxn.pay.module.union.config.UnionPayProperties;
import com.zeroxn.pay.module.union.constant.UnionConstant;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: lisang
 * @DateTime: 2023/6/2 上午9:07
 * @Description:
 */
@Component
@Conditional(ConditionalOnUnion.class)
public class UnionUtils {
    private static final Logger logger = LoggerFactory.getLogger(UnionUtils.class);
    private static PayCertManager certManager;
    private static UnionPayProperties properties;

    @Autowired
    public void setUnionPayCertManager(PayCertManager certManager){
        UnionUtils.certManager = certManager;
    }
    @Autowired
    public void setUnionPayProperties(UnionPayProperties properties) {
        UnionUtils.properties = properties;
    }
    /**
     * 过滤Map中的空Value键值对
     * @param data 入参Map
     * @return 返回过滤空键值对后的Map
     */
    public static Map<String, String> filterEmpty(Map<String, String> data){
        return data.entrySet().stream().filter(entry ->
                !isEmpty(entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 对请求数据进行签名
     * @param data 请求数据
     * @return 返回加了签名后的请求数据
     */
    public static Map<String, String> sign(Map<String, String> data){
        Map<String, String> filterMap = filterEmpty(data);
        try{
            String signMethod = filterMap.get("signMethod");
            String charset = properties.getCharset();
            if(UnionConstant.SIGN_METHOD_RSA.equals(signMethod)){
                String certPath = properties.getSignCertPath();
                return signByRsa(filterMap, certPath, charset);
            } else if (UnionConstant.SIGN_METHOD_SHA256.equals(signMethod)){
                String signKey = properties.getSignKey();
                return signBySha256(filterMap, signKey, charset);
            }else {
                String signKey = properties.getSignKey();
                return signBySm3(filterMap, signKey, charset);
            }
        }catch (Exception ex){
            UnionUtils.logger.error("请求数据签名失败，错误消息：{}", ex.getMessage());
            throw new PaySystemException("云闪付参数签名失败");
        }
    }

    /**
     * 云闪付后台通知验签
     * @param reqData 请求数据
     * @return 成功返回true否则false
     */
    public static boolean validateSign(Map<String, String> reqData) throws Exception {
        String charset = reqData.get("encoding");
        if (isEmpty(charset)){
            charset = "UTF-8";
        }
        String signMethod = reqData.get("signMethod");
        if (isEmpty(signMethod)){
            // 01表示RSA
            signMethod = UnionConstant.SIGN_METHOD_RSA;
        }
        String signature = reqData.remove("signature");
        if (isEmpty(signature)){
            logger.error("签名为空，验签失败");
            return false;
        }
        if(UnionConstant.SIGN_METHOD_RSA.equals(signMethod)){
            String publicKeyStr = reqData.get("signPubKeyCert");
            PublicKey publicKey = generateVerifyKey(publicKeyStr);
            if(publicKey == null){
                logger.error("公钥证书生成失败");
                return false;
            }
            String stringData = mapToString(reqData, charset, true, false);
            byte[] sha256 = sha256(stringData.getBytes(charset));
            String hexString = bytesToHexString(sha256);
            Signature signatureVerifier = Signature.getInstance("SHA256withRSA");
            signatureVerifier.initVerify(publicKey);
            signatureVerifier.update(hexString.getBytes(charset));
            return signatureVerifier.verify(Base64.getDecoder().decode(signature.getBytes(charset)));
        } else if (UnionConstant.SIGN_METHOD_SHA256.equals(signMethod) || UnionConstant.SIGN_METHOD_SM3.equals(signMethod)) {
            String signKey = properties.getSignKey();
            String reqStr = mapToString(reqData, charset, true, false);
            String newSignature = claSign(reqStr, signKey, charset, signMethod);
            return newSignature.equals(signature);
        }else {
            logger.error("不支持的签名方式，{}", signMethod);
            return false;
        }
    }

    // 字符串判空
    public static boolean isEmpty(CharSequence value){
        return value == null || value.length() == 0;
    }

    /**
     * RSA加密
     * @param data 请求数据
     * @param certPath 商户私钥证书路径
     * @param charset 编码格式
     * @return
     * @throws Exception
     */
    private static Map<String, String> signByRsa(Map<String, String> data, String certPath, String charset) throws Exception {
        String certId = UnionUtils.certManager.getCertId(certPath);
        data.put("certId", certId);
        String reqStr = mapToString(data, charset, true, false);
        byte[] bytes = sha256(reqStr.getBytes(charset));
        String sha256Hex = bytesToHexString(bytes).toLowerCase();
        String signature = Base64.getEncoder().encodeToString(signatureSHA256(UnionUtils.certManager.getPrivateKey(certPath), sha256Hex.getBytes()));
        data.put("signature", signature);
        return data;
    }

    /**
     * sha256摘要签名
     * @param data 请求数据
     * @param signKey 摘要签名的密钥字符串
     * @param charset 编码格式
     * @return 返回添加了签名数据的请求数据
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private static Map<String, String> signBySha256(Map<String, String> data, String signKey, String charset) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String reqStr = mapToString(data, charset, true, false);
        String signature = claSign(reqStr, signKey, charset, UnionConstant.SIGN_METHOD_SHA256);
        data.put("signature", signature);
        return data;
    }

    /**
     * sm3摘要签名
     * @param data 请求数据
     * @param signKey 摘要签名的密钥字符串
     * @param charset 编码格式
     * @return 返回添加了签名数据的请求数据
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private static Map<String, String> signBySm3(Map<String, String> data, String signKey, String charset) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String reqStr = mapToString(data, charset, true, false);
        String signature = claSign(reqStr, signKey, charset, UnionConstant.SIGN_METHOD_SM3);
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
                if(isEncode && !UnionUtils.isEmpty(value)){
                    value = URLEncoder.encode(value, charset);
                }
                builder.append(key).append("=").append(value).append("&");
            }
            return builder.substring(0, builder.length() - 1);
        }catch (UnsupportedEncodingException ex){
            UnionUtils.logger.error("Map集合转字符串失败，错误消息：{}", ex.getMessage());
            throw new PaySystemException("云闪付返回参数转换失败");
        }
    }
    
    /**
     * 通过密钥字符串和签名方式获取字符串数据的签名值
     * @param reqStr 需要签名的字符串
     * @param signKey 密钥字符串
     * @param charset 编码格式
     * @param signMethod 签名方式
     * @return 待签名字符串的签名值
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private static String claSign(String reqStr, String signKey, String charset, String signMethod) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(UnionConstant.SIGN_METHOD_SHA256.equals(signMethod)){
            String value = reqStr + "&" + bytesToHexString(sha256(signKey.getBytes(charset)));
            return bytesToHexString(sm3(value.getBytes(charset)));
        }else {
            String value = reqStr + "&" + bytesToHexString(sm3(signKey.getBytes(charset)));
            return bytesToHexString(sm3(value.getBytes(charset)));
        }
    }

    /**
     * 通过双分隔符将字符串转换为Map<String, String>集合
     * @param str 原始字符串
     * @param split 第一次分割的分割符 分割出单组数据
     * @param separate 第二次分割的分割符 分割出KEY VALUE
     * @return 分割后的MAP集合
     */
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

    /**
     * 通过私钥对数据进行RSA SHA256加密
     * @param privateKey 商户私钥
     * @param bytes 需要加密的数据
     * @return 加密后的数据
     * @throws Exception
     */
    private static byte[] signatureSHA256(PrivateKey privateKey, byte[] bytes) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(bytes);
        return signature.sign();
    }

    /**
     * sha256签名
     * @param bytes 原始数据
     * @return 返回签名后的字节数组
     * @throws NoSuchAlgorithmException
     */
    private static byte[] sha256(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(bytes);
        return digest.digest(bytes);
    }

    /**
     * sm3签名
     * @param bytes 原始数据
     * @return 返回签名后的字节数组
     */
    private static byte[] sm3 (byte[] bytes){
        SM3Digest digest = new SM3Digest();
        digest.update(bytes, 0, bytes.length);
        byte[] encryptedBytes = new byte[digest.getDigestSize()];
        digest.doFinal(encryptedBytes, 0);
        return encryptedBytes;
    }

    /**
     * 将字节转换为16进制字符串
     * @param bytes 原始字节数组
     * @return 转换后的16进制字符串
     */
    private static String bytesToHexString(byte[] bytes){
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /**
     * 将公钥证书字符串转换为公钥证书
     * @param publicKeyStr 公钥证书字符串
     * @return 转后后的公钥证书
     */
    private static PublicKey generateVerifyKey(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String formatKey = publicKeyStr.replaceAll("\\n", "").replaceAll("\\s", "");
        byte[] publicKeyBytes = Base64.getDecoder().decode(formatKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static <K, V> MultiValueMap<K, V> mapToMultiValueMap(Map<K, V> sourceMap){
        MultiValueMap<K, V> multiValueMap = new LinkedMultiValueMap<>();
        sourceMap.forEach(multiValueMap::add);
        return multiValueMap;
    }
}
