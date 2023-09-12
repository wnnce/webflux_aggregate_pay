package com.zeroxn.pay.module.jdpay.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeroxn.pay.core.amqp.PayMQTemplate;
import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;
import com.zeroxn.pay.module.jdpay.business.JdPayBusiness;
import com.zeroxn.pay.module.jdpay.config.ConditionalOnJdPay;
import com.zeroxn.pay.module.jdpay.model.JdPayMap;
import com.zeroxn.pay.module.jdpay.utils.JdPayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023-09-11 10:30:21
 * @Description: 京东支付Service层
 */
@Service
@Conditional(ConditionalOnJdPay.class)
public class JdPayService {
    private static final Logger logger = LoggerFactory.getLogger(JdPayService.class);
    private final JdPayBusiness business;

    private final PayMQTemplate mqTemplate;

    public JdPayService(JdPayBusiness business, PayMQTemplate mqTemplate) {
        this.business = business;
        this.mqTemplate = mqTemplate;
    }

    public Map<String, Object> jdPayConfirmOrder(JdPayParamsDto paramsDto) {
        JdPayMap<String, String> params = new JdPayMap<>();
        params.put("tradeNum", paramsDto.getOrderId());
        params.put("tradeName", paramsDto.getTradeName());
        params.put("tradeDesc", paramsDto.getTradeDesc());
        params.put("amount", paramsDto.getTotal().toString());
        params.put("orderType", paramsDto.getOrderType().toString());
        params.put("callbackUrl", paramsDto.getReturnUrl());
        params.put("userId", paramsDto.getUserId());
        params.put("bizTp", paramsDto.getChannelType());
        String reqPlat = paramsDto.getTerraceType() == 0 ? "H5" : "PC";
        params.put("reqPlat", reqPlat);
        return business.confirmOrder(params);
    }

    public Map<String, Object> jdPayQueryOrder(String orderId) {
        return business.queryOrder(orderId);
    }

    public Map<String, Object> jdPayCloseOrder(JdPayParamsDto paramsDto) {
        return business.orderRefund(paramsDto.getOrderId(), paramsDto.getRefundId(), paramsDto.getTotal());
    }

    public Map<String, Object> jdPayRefundOrder(JdPayParamsDto paramsDto) {
        return business.orderRefund(paramsDto.getOrderId(), paramsDto.getRefundId(), paramsDto.getRefundAmount());
    }

    public Map<String, Object> jdPayQueryRefundOrder(String orderId, String refundId) {
        return business.queryRefundOrder(orderId, refundId);
    }

    public String jdPaySuccessNotify(Map<String, Object> params) {
        params = handleNotifyParams(params);
        if (params == null) {
            return null;
        }
        String orderId = String.valueOf(params.get("tradeNum"));
        if (orderId == null || orderId.isEmpty()) {
            logger.error("该通知中不存在原订单流水号，通知内容：{}", params);
            return null;
        }
        logger.info("异步通知解密成功，订单号：{}支付成功", orderId);
        mqTemplate.send(PayPlatform.JDPAY, PayResult.SUCCESS, orderId);
        return "ok";
    }

    public String jdPayRefundNotify(Map<String, Object> params) {
        params = handleNotifyParams(params);
        if (params == null) {
            return null;
        }
        String orderId = String.valueOf(params.get("oTradeNum"));
        if (orderId == null || orderId.isEmpty()) {
            logger.error("该通知中不存在原订单流水号，通知内容：{}", params);
            return null;
        }
        logger.info("异步通知解密成功，订单号：{}退款成功", orderId);
        mqTemplate.send(PayPlatform.JDPAY, PayResult.REFUND, orderId);
        return "ok";
    }

    private Map<String, Object> handleNotifyParams(Map<String, Object> params) {
        logger.info("接收到异步通知，交易返回码：{}", params.get("result"));
        if (!params.containsKey("encrypt")){
            logger.warn("异步通知不包含encrypt标签，忽略处理");
            return null;
        }
        String encrypt = String.valueOf(params.get("encrypt"));
        String requestData = JdPayUtils.decryptNotify(encrypt);
        return JdPayUtils.xmlToObject(requestData, new TypeReference<Map<String, Object>>() {});
    }
}
