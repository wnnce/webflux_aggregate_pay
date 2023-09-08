package com.zeroxn.pay.module.paypal.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.zeroxn.pay.core.exception.PayServiceException;
import com.zeroxn.pay.module.paypal.config.PaypalProperties;
import com.zeroxn.pay.module.paypal.constant.PaypalConstant;
import com.zeroxn.pay.module.paypal.modle.AuthResult;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public ResponseEntity<Map<String, Object>> createOrder(String referenceId, String description, Integer price, String returnUrl, String cancelUrl){
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
        return sendRequest(PaypalConstant.CREATE_ORDER_URL, HttpMethod.POST, entity);
    }

    public ResponseEntity<Map<String, Object>> confirmOrder(String orderId){
        String url = PaypalConstant.CAPTURE_ORDER_URL.replace("{id}", orderId);
        HttpHeaders headers = makeRequestHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return sendRequest(url, HttpMethod.POST, entity);
    }

    public ResponseEntity<Map<String, Object>> queryOrder(String orderId) {
        String url = PaypalConstant.QUERY_ORDER_URL.replace("{id}", orderId);
        HttpHeaders headers = makeRequestHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return sendRequest(url, HttpMethod.GET, entity);
    }

    public ResponseEntity<Map<String, Object>> refundOrder(String captureId) {
        String url = PaypalConstant.REFUND_ORDER_URL.replace("{id}", captureId);
        HttpEntity<String> entity = new HttpEntity<>(makeRequestHeaders());
        return sendRequest(url, HttpMethod.POST, entity);
    }

    public ResponseEntity<Map<String, Object>> queryRefundOrder(String refundId) {
        String url = PaypalConstant.QUERY_REFUND_URL.replace("{id}", refundId);
        HttpEntity<String> entity = new HttpEntity<>(makeRequestHeaders());
        return sendRequest(url, HttpMethod.GET, entity);
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

    private <T> ResponseEntity<Map<String, Object>> sendRequest(String url, HttpMethod method, HttpEntity<T> entity) {
        try{
            return restTemplate.exchange(
                    url, method, entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {});
        }catch (RestClientException e) {
            logger.error("PayPal请求失败，错误消息：{}", e.getMessage());
            throw new PayServiceException(handleErrorResponse(e.getMessage()));
        }
    }

    private HttpHeaders makeRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getToken());
        return headers;
    }

    private String handleErrorResponse(String errorMessage)  {
        String errorCode = errorMessage.substring(0, errorMessage.indexOf(':'));
        String regex = "\"message\":\"([^\"]*)\"";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(errorMessage);
        String message = "";
        if (matcher.find()) {
            message = matcher.group(1);
        }
        return errorCode + "  " + message;
    }
}