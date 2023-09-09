package com.zeroxn.pay.test;

import com.zeroxn.pay.module.jdpay.utils.JdPayUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 16:03:43
 * @Description:
 */
public class JdPayTest {
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
        MultiValueMap<String, String> multiValueMap = JdPayUtils.mapToMultiValueMap(map);
        System.out.println(multiValueMap);
        long endTime = System.currentTimeMillis();
        System.out.println("handleTime:" + (endTime - startTime));
    }
}
