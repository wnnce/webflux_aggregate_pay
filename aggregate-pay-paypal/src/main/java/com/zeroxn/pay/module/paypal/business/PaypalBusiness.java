package com.zeroxn.pay.module.paypal.business;

import com.zeroxn.pay.module.paypal.config.PaypalProperties;
import com.zeroxn.pay.module.paypal.constant.PaypalConstant;
import com.zeroxn.pay.module.paypal.dto.AuthResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午1:01
 * @Description: Paypal业务类
 */
public class PaypalBusiness {
    private static final Logger logger = LoggerFactory.getLogger(PaypalBusiness.class);
    private final PaypalProperties properties;
    private final RestTemplate restTemplate;
    public PaypalBusiness(PaypalProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    @Async
    public void initAuthorizationToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(properties.getClientId(), properties.getSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(PaypalConstant.AUTH_FORM_KEY, PaypalConstant.AUTH_FORM_VALUE);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
        ResponseEntity<AuthResult> response = restTemplate.exchange(PaypalConstant.AUTH_URL, HttpMethod.POST, entity, AuthResult.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null){
            String token = response.getBody().access_token();
            logger.info("获取Paypal Token成功，Token：{}", token);
            properties.setToken(token);
        }else {
            logger.error("获取PaypalToken失败...");
        }
    }
}