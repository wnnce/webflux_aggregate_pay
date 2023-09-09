package com.zeroxn.pay.module.paypal.web;

import com.zeroxn.pay.module.paypal.business.PaypalBusiness;
import com.zeroxn.pay.module.paypal.config.ConditionalOnPayPal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023-09-08 13:04:14
 * @Description: PayPal支付Service层
 */
@Service
@Conditional(ConditionalOnPayPal.class)
public class PayPalService {
    private static final Logger logger = LoggerFactory.getLogger(PayPalService.class);
    private final PaypalBusiness paypalBusiness;
    public PayPalService(PaypalBusiness paypalBusiness){
        this.paypalBusiness = paypalBusiness;
    }

    /**
     * PayPal创建订单，调用paypalBusiness中的创建订单方法并拿到请求结果，然后对请求结果进行处理。
     * 请求成功时直接返回响应体，否则抛出支付业务异常，错误消息为响应体
     * @param params 创建订单需要的参数
     * @return 创建结果
     */
    public Map<String, Object> paypalCreateOrder(PayPalParamsDTO params) {
        ResponseEntity<Map<String, Object>> response = paypalBusiness.createOrder(params.getReferenceId(), params.getDescription(),
                params.getTotal(), params.getReturnUrl(), params.getCancelUrl());
        return handleResponse(response);
    }

    /**
     * PayPal确认订单收款，调用paypalBusiness的收款方法，然后对请求结果进行处理
     * @param orderId PayPal创建订单成功后的订单ID
     * @return 返回收款结果
     */
    public Map<String, Object> paypalConfirmOrder(String orderId) {
        ResponseEntity<Map<String, Object>> response = paypalBusiness.confirmOrder(orderId);
        return handleResponse(response);
    }

    /**
     * 查询PayPal生成的订单，通过PayPal内的订单ID查询，不是创建订单时的预付款ID
     * @param orderId PayPal订单内ID
     * @return 返回订单详情
     */
    public Map<String, Object> queryPayPalOrder(String orderId) {
        ResponseEntity<Map<String, Object>> response = paypalBusiness.queryOrder(orderId);
        return handleResponse(response);
    }
    public Map<String, Object> paypalOrderRefund(String captureId) {
        ResponseEntity<Map<String, Object>> response = paypalBusiness.refundOrder(captureId);
        return handleResponse(response);
    }
    public Map<String, Object> queryPayPalRefundOrder(String refundId) {
        ResponseEntity<Map<String, Object>> response = paypalBusiness.queryRefundOrder(refundId);
        return handleResponse(response);
    }

    private Map<String, Object> handleResponse(ResponseEntity<Map<String, Object>> response) {
        return response.getBody();
    }
}
