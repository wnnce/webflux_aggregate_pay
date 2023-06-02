package com.zeroxn.pay.module.union.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Author: lisang
 * @DateTime: 2023/6/2 上午9:07
 * @Description:
 */
public class UnionUtil {
    private static final Logger logger = LoggerFactory.getLogger(UnionUtil.class);
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
    public static Map<String, String> sign(Map<String, String> data, String privateKey, String charset){
        try{
            String reqStr = mapToString(data, charset);
            String signature = reqStrSha256(reqStr, privateKey, charset);
            data.put("signature", signature);
            return data;
        }catch (Exception ex){
            logger.error("请求数据签名失败，错误消息：{}", ex.getMessage());
            throw  new RuntimeException();
        }
    }

    // 字符串判空
    public static boolean isEmpty(String value){
        return value == null || value.length() == 0;
    }

    /**
     * 将Map集合中的数据转换为key=value&key=value的形式 并对中文进行URL编码
     * @param data Map集合
     * @param charset 编码格式默认 UTF-8
     * @return 返回经过URL编码后的数据字符串
     */
    public static String mapToString(Map<String, String> data, String charset) throws UnsupportedEncodingException {
        List<String> keyList = new ArrayList<>(data.keySet());
        StringBuilder builder = new StringBuilder();
        Collections.sort(keyList);
        for (String key : keyList) {
            String value = data.get(key);
            value = URLEncoder.encode(value, charset);
            builder.append(key).append("=").append(value).append("&");
        }
        return builder.substring(0, builder.length() - 1);
    }

    public static String reqStrSha256(String reqStr, String privateKey, String charset) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String value = reqStr + "&" + bytesToHexString(sha256(privateKey.getBytes(charset)));
        return bytesToHexString(sha256(value.getBytes(charset)));
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

}
