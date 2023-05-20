package com.zeroxn.pay.module.wechat.service.refund;

import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.QueryByOutRefundNoRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.module.wechat.constant.WechatConstant;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 13:00
 * @Description: 微信支付退款服务类 支持所有平台的退款
 */
public class WechatRefundService {
    private static final RefundService service;
    static {
        service = new RefundService.Builder().config(WechatConstant.getConfig()).build();
    }

    /**
     * 单笔订单退款
     * @param param 封装退款请求参数
     * @return 返回退款信息
     */
    public static Refund orderRefund(PayParams param){
        AmountReq amount = new AmountReq();
        amount.setRefund(Long.valueOf(param.getWechatRefundTotal()));
        amount.setTotal(Long.valueOf(param.getWechatTotal()));
        amount.setCurrency("CNY");

        CreateRequest request = new CreateRequest();
        request.setOutTradeNo(param.getOrderId());
        request.setOutRefundNo(param.getOrderRefundId());
        request.setNotifyUrl(WechatConstant.getRefundNotifyUrl());
        request.setAmount(amount);
        return service.create(request);
    }

    /**
     * 通过订单退款id 查询单笔退款
     * @param orderRefundId 商户系统内订单退款id
     * @return 返回退款信息
     */
    public static Refund queryRefund(String orderRefundId){
        QueryByOutRefundNoRequest request = new QueryByOutRefundNoRequest();
        request.setOutRefundNo(orderRefundId);
        return service.queryByOutRefundNo(request);
    }
}
