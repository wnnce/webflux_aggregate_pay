package com.zeroxn.pay.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.zeroxn.pay.module.jdpay.business.JdPayBusiness;
import com.zeroxn.pay.module.jdpay.model.JdPayMap;
import com.zeroxn.pay.module.jdpay.utils.JdPayUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 16:03:43
 * @Description:
 */
public class JdPayTest {
    private static final Logger logger = LoggerFactory.getLogger(JdPayTest.class);
    @Autowired
    private JdPayBusiness jdPayBusiness;

    @Test
    public void testMapFilter() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "");
        System.out.println(map);
        Map<String, String> result = JdPayUtils.filterMapByEmpty(map);
        System.out.println(result);
    }

    @Test
    public void testSign() throws Exception {
        long startTime = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        map.put("orderId", "AB465446");
        map.put("total", "1230");
        map.put("title", "测试商品");
        map.put("info", "");
        System.out.println(map);
        map = JdPayUtils.filterMapByEmpty(map);
        System.out.println(map);
        String paramsStr = JdPayUtils.getParamsStr(map);
        System.out.println(paramsStr);
        String sha265 = JdPayUtils.bytesToHexString(JdPayUtils.sha256(paramsStr.getBytes()));
        System.out.println(sha265);
        String sign = JdPayUtils.sign(paramsStr);
        MultiValueMap<String, String> multiValueMap = JdPayUtils.mapToEncryptMultiValueMap(map);
        multiValueMap.add("sign", sign);
        System.out.println(multiValueMap);
        long endTime = System.currentTimeMillis();
        System.out.println("handleTime:" + (endTime - startTime));
    }

    @Test
    public void testWapConfirmOrder() {
        JdPayMap<String, String> map = new JdPayMap<>();
        map.put("tradeNum", "SADAS456456161");
        map.put("tradeName", "测试商品");
        map.put("amount", "1");
        map.put("orderType", "1");
        map.put("userId", "464161879846");
        Map<String, Object> response = jdPayBusiness.confirmOrder(map);
        System.out.println(response);
    }

    @Test
    public void testJacksonXml() throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        Map<String, String> map = new JdPayMap<>();
        map.put("tradeNum", "SADAS456456161");
        map.put("tradeName", "测试商品");
        map.put("amount", "1");
        map.put("orderType", "1");
        map.put("userId", "464161879846");
        String xmlString = xmlMapper.writeValueAsString(map);
        String sign = JdPayUtils.sign(xmlString);
        map.put("sign", sign);
        xmlString = xmlMapper.writeValueAsString(map);
        String encryptString = JdPayUtils.encryptBy3DES(xmlString);
        Map<String, String> requestMap = new JdPayMap<>();
        requestMap.put("version", "2.0");
        requestMap.put("meid", "216161");
        requestMap.put("encrypt", encryptString);
        System.out.println(xmlMapper.writeValueAsString(requestMap));
    }

    @Test
    public void test3DESEncryptAndDecrypt() throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        Map<String, String> map = Map.ofEntries(
                Map.entry("name", "xiao"),
                Map.entry("age", "18"),
                Map.entry("gender", "男"),
                Map.entry("location", "重庆"),
                Map.entry("ip", "192.168.1.1")
        );
        logger.info(map.toString());
        String xmlString = xmlMapper.writeValueAsString(map);
        String encrypt = JdPayUtils.encryptBy3DES(xmlString);
        logger.info(encrypt);
        String decrypt = JdPayUtils.decryptBy3DES(encrypt);
        logger.info(decrypt);
        Map<String, String> map1 = JdPayUtils.xmlToObject(decrypt, new TypeReference<Map<String, String>>() {});
        logger.info(map1.toString());
    }
}
