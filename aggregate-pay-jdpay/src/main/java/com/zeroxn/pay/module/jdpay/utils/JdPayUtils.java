package com.zeroxn.pay.module.jdpay.utils;


import com.zeroxn.pay.core.exception.PaySystemException;
import com.zeroxn.pay.module.jdpay.config.JdPayProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 15:54:42
 * @Description: 京东支付工具类
 */
@Component
public class JdPayUtils {
    private static final Logger logger = LoggerFactory.getLogger(JdPayUtils.class);
    private static JdPayProperties properties;
    @Autowired
    public void setJdPayProperties(JdPayProperties properties){
        JdPayUtils.properties = properties;
    }


    public static <K, V> Map<K, V> filterMapByEmpty(Map<K, V> map){
        return map.entrySet().stream().filter(entry -> entry.getValue() != null && !String.valueOf(entry.getValue()).isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K, V> MultiValueMap<K, V> mapToMultiValueMap(Map<K, V> map) {
        MultiValueMap<K, V> multiValueMap = new LinkedMultiValueMap<>();
        map.forEach(multiValueMap::add);
        return multiValueMap;
    }

    public static String getParamsStr(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> keyList = new ArrayList<>(map.keySet().stream().toList());
        Collections.sort(keyList);
        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            String value = map.get(key);
            if (i == keyList.size() - 1){
                stringBuilder.append(key).append("=").append(value);
            }else {
                stringBuilder.append(key).append("=").append(value).append("&");
            }
        }
        return stringBuilder.toString();
    }

    public static String sign(String paramStr) {
        try{
            byte[] bytes = sha256(paramStr.getBytes(StandardCharsets.UTF_8));
            PrivateKey privateKey = properties.getPrivateKey();
            return bytesToHexString(sha26WithRsa(privateKey, bytes));
        } catch (Exception e) {
            logger.error("京东支付请求签名失败，错误消息：{}", e.getMessage());
            throw new PaySystemException("请求签名失败");
        }
    }
    public static void encryptBy3DES() {

    }
    public static byte[] sha256(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(bytes);
        return digest.digest();
    }

    public static byte[] sha26WithRsa(PrivateKey privateKey, byte[] bytes) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(bytes);
        return signature.sign();
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static <T extends Key> T readKeyFromString(String keyString, Class<T> keyClass) throws Exception {
        String cleanKey = keyString.trim();
        byte[] privateKeyBytes = Base64.getDecoder().decode(cleanKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        if (keyClass.equals(PublicKey.class)){
            return (T) keyFactory.generatePublic(keySpec);
        } else if (keyFactory.equals(PrivateKey.class)) {
            return (T) keyFactory.generatePrivate(keySpec);
        }else {
            throw new RuntimeException("Key的Class类型不被支持");
        }
    }
}
