package com.zeroxn.pay.module.union;

import com.zeroxn.pay.core.PayTemplate;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.exception.PayServiceException;
import com.zeroxn.pay.module.union.business.UnionPayBusiness;
import com.zeroxn.pay.module.union.config.UnionPayProperties;
import com.zeroxn.pay.module.union.constant.UnionConstant;
import com.zeroxn.pay.module.union.utils.UnionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 下午7:42
 * @Description:
 */
@Qualifier("unionPayTemplate")
public class UnionPayTemplate implements PayTemplate {
    private final UnionPayProperties properties;
    private final UnionPayBusiness business;
    public UnionPayTemplate(UnionPayBusiness business, UnionPayProperties properties){
        this.properties = properties;
        this.business = business;
    }

    @NotNull
    @Override
    public String getPlatformName() {
        return PayPlatform.UNION.getValue();
    }

    @Override
    public <T> T confirmOrder(PayParams param, PayMethod method, Class<T> clazz) {
        Map<String, String> requestData = this.generateBaseRequestData();
        Map<String, String> frontData = new HashMap<>(Map.ofEntries(
                Map.entry("txnType", "02"),
                Map.entry("txnSubType", "01"),
                Map.entry("currencyCode", properties.getCurrency()),
                Map.entry("backUrl", properties.getSuccessNotifyUrl()),
                Map.entry("orderId", param.getOrderId()),
                Map.entry("txnAmt", param.getUnionTotal().toString()),
                Map.entry("frontUrl", param.getFrontUrl()),
                Map.entry("orderDesc", param.getDescription())
        ));
        requestData.putAll(frontData);
        switch (method){
            case DESKTOP -> {
                requestData.put("channelType", UnionConstant.DESKTOP_CHANNEL_TYPE);
            }default -> {
                requestData.put("channelType", UnionConstant.WAP_CHANNEL_TYPE);
                return (T) business.wapConfirmOrder(requestData);
            }
        }
        return null;
    }

    @Override
    public <T> T closeOrder(String orderId, PayMethod method, Class<T> clazz) {
        String result = queryOrder(orderId, null, String.class);
        Map<String, String> map = UnionUtils.stringToMap(result, "&", "=");
        if(UnionUtils.isEmpty(map.get("queryId")) || !map.get("respMsg").contains("成功")){
            System.out.println(map);
            throw new PayServiceException("云闪付关闭订单失败，订单状态错误");
        }
        String txnAmt = map.get("txnAmt");
        String queryId = map.get("queryId");
        Map<String, String> requestData = generateBaseRequestData();
        Map<String, String> closeMap = new HashMap<>(Map.ofEntries(
                Map.entry("orderId", orderId),
                Map.entry("txnType", "31"),
                Map.entry("txnSubType", "00"),
                Map.entry("txnAmt", txnAmt),
                Map.entry("origQryId", queryId),
                Map.entry("channelType", UnionConstant.WAP_CHANNEL_TYPE),
                Map.entry("backUrl", "http://www.specialUrl.com")
        ));
        requestData.putAll(closeMap);
        return (T) business.wapRevokeOrder(requestData);
    }

    @Override
    public <T> T queryOrder(String orderId, PayMethod method, Class<T> clazz) {
        Map<String, String> requestData = this.generateBaseRequestData();
        Map<String, String> queryMap = new HashMap<>(Map.ofEntries(
                Map.entry("txnType", "00"),
                Map.entry("txnSubType", "00"),
                Map.entry("orderId", orderId)
        ));
        requestData.putAll(queryMap);
        return (T) business.queryOrder(requestData);
    }

    @Override
    public <T> T refundOrder(PayParams param, Class<T> clazz) {
        Map<String, String> requestData = this.generateBaseRequestData();
        Map<String, String> refundMap = new HashMap<>(Map.ofEntries(
                Map.entry("txnType", "04"),
                Map.entry("txnSubType", "00"),
                Map.entry("orderId", param.getOrderId()),
                Map.entry("currencyCode", properties.getCurrency()),
                Map.entry("txnAmt", param.getRefundTotal().toString()),
                Map.entry("backUrl", properties.getRefundNotifyUrl()),
                Map.entry("origQryId", param.getOrderRefundId()),
                Map.entry("channelType", UnionConstant.DESKTOP_CHANNEL_TYPE)
        ));
        requestData.putAll(refundMap);
        return (T) business.refundOrder(requestData);
    }

    @Override
    public <T> T queryRefundOrder(String orderId, String orderRefundId, Class<T> clazz) {
        return null;
    }

    private Map<String, String> generateBaseRequestData(){
        return new HashMap<>(Map.ofEntries(
                Map.entry("version", UnionConstant.VERSION),
                Map.entry("encoding", properties.getCharset()),
                Map.entry("signMethod", properties.getSignType()),
                Map.entry("bizType", UnionConstant.BIZ_TYPE),
                Map.entry("merId", properties.getMerchantId()),
                Map.entry("accessType", UnionConstant.ACCESS_TYPE),
                Map.entry("txnTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
        ));
    }
}
