package com.zeroxn.pay.module.jdpay.business;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeroxn.pay.core.exception.PayServiceException;
import com.zeroxn.pay.core.exception.PaySystemException;
import com.zeroxn.pay.module.jdpay.config.JdPayProperties;
import com.zeroxn.pay.module.jdpay.constant.JdPayConstant;
import com.zeroxn.pay.module.jdpay.model.JdPayMap;
import com.zeroxn.pay.module.jdpay.utils.JdPayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023-09-09 15:09:32
 * @Description: 京东支付业务类
 */
public class JdPayBusiness {
    private static final Logger logger = LoggerFactory.getLogger(JdPayBusiness.class);
    private final JdPayProperties properties;
    private final RestTemplate restTemplate;

    public JdPayBusiness(JdPayProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    /**
     * 京东支付统一下单，包含H5下单和电脑网站下单
     * @param params 封装下单参数
     * @return 返回京东支付平台的响应结果
     */
    public Map<String, Object> confirmOrder(JdPayMap<String, String> params) {
        makeParamsMap(params);
        params.put("tradeTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        params.put("currency", properties.getCurrency());
        params.put("notifyUrl", properties.getSuccessNotifyUrl());
        params = JdPayUtils.handleRequestDataEncrypt(params);
        return sendRequest(JdPayConstant.CONFIRM_ORDER_URL, HttpMethod.POST, JdPayUtils.toXml(params));
    }

    /**
     * 京东支付订单查询，提供下单时的订单流水号（订单ID）即可查询
     * @param orderId 订单流水号（订单ID）
     * @return 返回订单的详细信息
     */
    public Map<String, Object> queryOrder(String orderId){
        JdPayMap<String, String> params = new JdPayMap<>();
        makeParamsMap(params);
        params.put("tradeNum", orderId);
        params.put("tradeType", "0");
        params = JdPayUtils.handleRequestDataEncrypt(params);
        return sendRequest(JdPayConstant.QUERY_ORDER_URL, HttpMethod.POST, JdPayUtils.toXml(params));
    }

    /**
     * 京东支付查询退款订单，需要下单时的订单流水号和申请退款时的退款流水号
     * @param orderId 订单流水号（订单ID）
     * @param refundId 订单退款流水号
     * @return 返回退款订单的详细信息
     */
    public Map<String, Object> queryRefundOrder(String orderId, String refundId) {
        JdPayMap<String, String> params = new JdPayMap<>();
        makeParamsMap(params);
        params.put("tradeNum", refundId);
        params.put("oTradeNum", orderId);
        params.put("tradeType", "1");
        params = JdPayUtils.handleRequestDataEncrypt(params);
        return sendRequest(JdPayConstant.QUERY_ORDER_URL, HttpMethod.POST, JdPayUtils.toXml(params));
    }

    /**
     * 京东支付订单退款，订单可以多次退款，只要总退款金额没有大于等于订单支付金额
     * 每次申请退款都需要一个唯一的退款流水号
     * @param orderId 订单流水号（订单ID）
     * @param refundId 退款流水号，多次退款需不同
     * @param amount 退款金额，单位：分
     * @return 返回订单退款的详细信息
     */
    public Map<String, Object> orderRefund(String orderId, String refundId, Integer amount) {
        JdPayMap<String, String> params = new JdPayMap<>();
        makeParamsMap(params);
        params.put("currency", properties.getCurrency());
        params.put("notifyUrl", properties.getRefundNotifyUrl());
        params.put("tradeNum", refundId);
        params.put("oTradeNum", orderId);
        params.put("amount", amount.toString());
        params = JdPayUtils.handleRequestDataEncrypt(params);
        return sendRequest(JdPayConstant.REFUND_ORDER_URL, HttpMethod.POST, JdPayUtils.toXml(params));
    }

    /**
     * 京东支付关闭订单，对于未支付的订单关闭后无法再支付。对于已经支付的订单则发起退款
     * @param orderId 订单流水号（订单ID）
     * @param refundId 撤销交易流水号 和订单流水号不能相同
     * @param amount 订单交易金额
     * @return 返回撤销订单结果
     */
    public Map<String, Object> closeOrder(String orderId, String refundId, Integer amount) {
        JdPayMap<String, String> params = new JdPayMap<>();
        makeParamsMap(params);
        params.put("currency", properties.getCurrency());
        params.put("tradeNum", refundId);
        params.put("oTradeNum", orderId);
        params.put("amount", amount.toString());
        params = JdPayUtils.handleRequestDataEncrypt(params);
        return sendRequest(JdPayConstant.REVOKE_ORDER_URL, HttpMethod.POST, JdPayUtils.toXml(params));
    }

    private void makeParamsMap(JdPayMap<String, String> params) {
        Map<String, String> tempMap = Map.ofEntries(
                Map.entry("version", properties.getVersion()),
                Map.entry("merchant", properties.getMerchantId())
        );
        params.putAll(tempMap);
    }

    private Map<String, Object> sendRequest(String url, HttpMethod method, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, method, entity,
                new ParameterizedTypeReference<Map<String, Object>>() {});
        if (response.getBody() == null){
            logger.error("请求京东支付失败，请求地址：{}", url);
            throw new PaySystemException("请求失败");
        }
        Map<String, Object> result = response.getBody();
        if (String.valueOf(result.get("result")).contains("000000")){
            String encryptString = String.valueOf(result.get("encrypt"));
            String decryptString = JdPayUtils.decryptBy3DES(encryptString);
            return JdPayUtils.xmlToObject(decryptString, new TypeReference<Map<String, Object>>() {});
        }else {
            logger.error("京东支付请求异常，返回消息：{}", result.get("result"));
            throw new PayServiceException(result.toString());
        }
    }

}
