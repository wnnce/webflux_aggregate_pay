package com.zeroxn.pay.module.jdpay.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.zeroxn.pay.core.config.PayCertManager;
import com.zeroxn.pay.core.exception.PaySystemException;
import com.zeroxn.pay.module.jdpay.config.ConditionalOnJdPay;
import com.zeroxn.pay.module.jdpay.config.JdPayProperties;
import com.zeroxn.pay.module.jdpay.model.JdPayMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
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
@Conditional(ConditionalOnJdPay.class)
public class JdPayUtils {
    private static final Logger logger = LoggerFactory.getLogger(JdPayUtils.class);
    private static final XmlMapper xmlMapper = new XmlMapper();
    private static PayCertManager certManager;
    private static JdPayProperties properties;

    static {
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    }

    @Autowired
    public void setPayCertManager(PayCertManager certManager) {
        JdPayUtils.certManager = certManager;
    }
    @Autowired
    public void setJdPayProperties(JdPayProperties properties){
        JdPayUtils.properties = properties;
    }


    public static <K, V> Map<K, V> filterMapByEmpty(Map<K, V> map){
        return map.entrySet().stream().filter(entry -> entry.getValue() != null && !String.valueOf(entry.getValue()).isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static MultiValueMap<String, String> mapToEncryptMultiValueMap(Map<String, String> map) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        map.forEach((key, value) -> {
            if (!"merchant".equals(key) && !"version".equals(key) && !"sign".equals(key)){
                value = encryptBy3DES(value);
            }
            multiValueMap.add(key, value);
        });
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

    public static JdPayMap<String, String> handleRequestDataEncrypt(JdPayMap<String, String> requestData) {
        String xmlString = JdPayUtils.toXml(requestData);
        String sign = JdPayUtils.sign(xmlString);
        requestData.put("sign", sign);
        String originString = JdPayUtils.toXml(requestData);
        String encryptString = JdPayUtils.encryptBy3DES(originString);
        JdPayMap<String, String> map = new JdPayMap<>();
        map.put("version", requestData.get("version"));
        map.put("merchant", requestData.get("merchant"));
        map.put("encrypt", encryptString);
        return map;
    }

    public static String sign(String paramStr) {
        try{
            byte[] bytes = sha256(paramStr.getBytes(StandardCharsets.UTF_8));
            PrivateKey privateKey = certManager.getPrivateKey(properties.getPrivateKeyPath());
            return bytesToHexString(sha26WithRsa(privateKey, bytes));
        } catch (Exception e) {
            logger.error("京东支付请求签名失败，错误消息：{}", e.getMessage());
            throw new PaySystemException("请求签名失败");
        }
    }

    public static String decryptNotify(String encrypt) {
        try{
            byte[] decodeBytes = Base64.getDecoder().decode(encrypt);
            Cipher cipher = Cipher.getInstance("RSA");
            PublicKey publicKey = certManager.getPublicKey(properties.getPublicKeyPath());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decryptBytes = cipher.doFinal(decodeBytes);
            return new String(decryptBytes, StandardCharsets.UTF_8);
        }catch (Exception e) {
            logger.error("京东支付通知解密失败，错误消息:{}", e.getMessage());
            throw new RuntimeException("通知解密失败");
        }
    }
    public static String decryptBy3DES(String encrypt) {
        try{
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, JdPayUtils.initSecretKey("0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF"));
            byte[] decodeBytes = Base64.getDecoder().decode(encrypt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = cipher.doFinal(decodeBytes);
            int len = (bytes[0] & 0xff) << 24 |
                    (bytes[1] & 0xff) << 16 |
                    (bytes[2] & 0xff) << 8 |
                    (bytes[3] & 0xff);
            byte[] plaintextWithPadding = new byte[len];
            System.arraycopy(bytes, 4, plaintextWithPadding, 0, len);
            return new String(plaintextWithPadding, StandardCharsets.UTF_8);
        }catch (Exception e){
            logger.error("3DES解密失败，错误消息：{}", e.getMessage());
            throw new PaySystemException("响应数据解析异常");
        }
    }
    public static String encryptBy3DES(String plaintext) {
        try{
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, JdPayUtils.initSecretKey("0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF"));
            byte[] originBytes = plaintext.getBytes(StandardCharsets.UTF_8);
            byte[] encryptBytes = cipher.doFinal(paddingBytes(originBytes));
            return Base64.getEncoder().encodeToString(encryptBytes);
        } catch (Exception e){
            logger.error("3DES加密失败，错误消息：{}", e.getMessage());
            throw new PaySystemException("请求数据异常");
        }
    }
    private static byte[] paddingBytes(byte[] bytes){
        int len = bytes.length;
        int x = (len + 4) % 8;
        int y = (x == 0) ? 0 : (8 - x);
        // 计算有效数据长度
        byte[] lengthBytes = new byte[4];
        lengthBytes[0] = (byte) ((len >> 24) & 0xFF);
        lengthBytes[1] = (byte) ((len >> 16) & 0xFF);
        lengthBytes[2] = (byte) ((len >> 8) & 0xFF);
        lengthBytes[3] = (byte) (len & 0xFF);
        // 创建新的字节数组，长度为原始字节数组长度加上有效数据长度长度加上补位长度
        byte[] result = new byte[len + 4 + y];
        // 将有效数据长度添加到头部
        System.arraycopy(lengthBytes, 0, result, 0, 4);
        // 复制原始字节数组到新数组后面
        System.arraycopy(bytes, 0, result, 4, len);
        // 补位
        for (int i = 0; i < y; i++) {
            result[len + 4 + i] = 0;
        }
        return result;
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
        byte[] keyBytes = Base64.getDecoder().decode(cleanKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        if (keyClass.equals(PublicKey.class)){
            return (T) keyFactory.generatePublic(keySpec);
        } else if (keyClass.equals(PrivateKey.class)) {
            return (T) keyFactory.generatePrivate(keySpec);
        }else {
            throw new RuntimeException("Key的Class类型不被支持");
        }
    }

    public static SecretKey initSecretKey(String keyString) throws Exception{
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(keyString.getBytes(StandardCharsets.UTF_8));
        return keyFactory.generateSecret(deSedeKeySpec);
    }

    public static String toXml(Object value) {
        try{
            return xmlMapper.writeValueAsString(value);
        }catch (JsonProcessingException e){
            logger.error("序列化为xml失败，错误消息：{}", e.getMessage());
            throw new PaySystemException("参数序列化失败");
        }
    }

    public static <T> T xmlToObject(String xmlString, TypeReference<T> valueRef) {
        try{
            return xmlMapper.readValue(xmlString, valueRef);
        }catch (JsonProcessingException e) {
            logger.error("反序列化xml失败，错误消息：{}", e.getMessage());
            throw new PaySystemException("返回参数反序列化失败");
        }

    }
}
