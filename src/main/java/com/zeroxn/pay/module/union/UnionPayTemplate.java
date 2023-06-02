package com.zeroxn.pay.module.union;

import com.zeroxn.pay.core.PayTemplate;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.module.union.business.UnionPayBusiness;
import com.zeroxn.pay.module.union.config.UnionPayProperties;
import com.zeroxn.pay.module.union.constant.UnionConstant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 下午7:42
 * @Description:
 */
public class UnionPayTemplate implements PayTemplate {
    private final UnionPayProperties properties;
    private final UnionPayBusiness business;
    public UnionPayTemplate(UnionPayBusiness business, UnionPayProperties properties){
        this.properties = properties;
        this.business = business;
    }

    @Override
    public <T> T confirmOrder(PayParams param, PayMethod method, Class<T> clazz) {
        Map<String, String> requestData = this.generateBaseRequestData();
        requestData.put("orderId", param.getOrderId());
        requestData.put("txnAmt", param.getUnionTotal().toString());
        requestData.put("frontUrl", param.getFrontUrl());
        requestData.put("orderDesc", param.getDescription());
        switch (method){
            case DESKTOP -> {
                requestData.put("channelType", UnionConstant.DESKTOPCHANNELTYPE);
            }default -> {
                requestData.put("channelType", UnionConstant.WAPCHANNELTYPE);
            }
        }
        return null;
    }

    @Override
    public <T> T closeOrder(String orderId, PayMethod method, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T queryOrder(String orderId, PayMethod method, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T refundOrder(PayParams param, Class<T> clazz) {
        return null;
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
                Map.entry("txnType", UnionConstant.TXNTYPE),
                Map.entry("txnSubType", UnionConstant.TXNSUBTYPE),
                Map.entry("bizType", UnionConstant.BIZTYPE),
                Map.entry("merId", properties.getMerchantId()),
                Map.entry("accessType", UnionConstant.ACCESSTYPE),
                Map.entry("currencyCode", properties.getCurrency()),
                Map.entry("txnTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMDDhhmmss"))),
                Map.entry("backUrl", properties.getNotifyUrl()),
                Map.entry("certId", properties.getCertId())
        ));
    }
}
