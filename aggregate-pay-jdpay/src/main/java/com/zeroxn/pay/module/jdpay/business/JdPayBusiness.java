package com.zeroxn.pay.module.jdpay.business;

import com.zeroxn.pay.module.jdpay.config.JdPayProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 15:09:32
 * @Description: 京东支付业务类
 */
public class JdPayBusiness {
    private static final Logger logger = LoggerFactory.getLogger(JdPayBusiness.class);

    private final JdPayProperties properties;

    public JdPayBusiness(JdPayProperties properties) {
        this.properties = properties;
    }

    public void wapConfirmOrder(Map<String, Object> params) {
        makeParamsMap(params);
        params.put("reqPlat", "H5");

    }

    private void makeParamsMap(Map<String, Object> params) {
        Map<String, Object> tempMap = Map.ofEntries(
                Map.entry("version", properties.getVersion()),
                Map.entry("merchant", properties.getMerchantId()),
                Map.entry("tradeTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))),
                Map.entry("currency", properties.getCurrency()),
                Map.entry("notifyUrl", properties.getNotifyUrl())
        );
        params.putAll(tempMap);
    }
}
