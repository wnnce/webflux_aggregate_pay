package com.zeroxn.pay.module.paypal.business;

import com.zeroxn.pay.core.exception.PayServiceException;
import com.zeroxn.pay.module.paypal.config.PaypalProperties;
import com.zeroxn.pay.module.paypal.constant.PaypalConstant;
import com.zeroxn.pay.module.paypal.dto.AuthResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

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

    public String createOrder(String referenceId, String description, Integer price, String returnUrl, String cancelUrl){
        HttpHeaders headers = new HttpHeaders();
        headers.set("PayPal-Request-Id", UUID.randomUUID().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getToken());
        String amount = String.format("%.2f", (double)price / 100);
        String requestBody = """
                {
                  "intent": "CAPTURE",
                  "purchase_units": [
                    {
                      "reference_id": "%s",
                      "description": "%s",
                      "amount": {
                        "currency_code": "%s",
                        "value": "%s"
                      }
                    }
                  ],
                  "payment_source": {
                    "paypal": {
                      "experience_context": {
                        "return_url": "%s",
                        "cancel_url": "%s"
                      }
                    }
                  }
                }
                """.formatted(referenceId, description, properties.getCurrency(), amount, returnUrl, cancelUrl).stripIndent().replaceAll("\n", "");
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(PaypalConstant.CREATE_ORDER_URL, HttpMethod.POST, entity, String.class);
        return handleResponse(response);
    }

    public String confirmOrder(String orderId){
        String url = PaypalConstant.CAPTURE_ORDER_URL.replace("{id}", orderId);
        HttpHeaders headers = makeRequestHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return handleResponse(response);
    }

    public String queryOrder(String orderId) {
        String url = PaypalConstant.QUERY_ORDER_URL.replace("{id}", orderId);
        HttpHeaders headers = makeRequestHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return handleResponse(response);
    }

    public String refundOrder(String captureId) {
        String url = PaypalConstant.REFUND_ORDER_URL.replace("{id}", captureId);
        HttpEntity<String> entity = new HttpEntity<>(makeRequestHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return handleResponse(response);
    }

    public String queryRefundOrder(String refundId) {
        String url = PaypalConstant.QUERY_REFUND_URL.replace("{id}", refundId);
        HttpEntity<String> entity = new HttpEntity<>(makeRequestHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return handleResponse(response);
    }

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

    private HttpHeaders makeRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getToken());
        return headers;
    }
    private String handleResponse(ResponseEntity<String> response) {
        if (!response.getStatusCode().is2xxSuccessful()){
            throw new PayServiceException(response.getBody());
        }
        return response.getBody();
    }
}