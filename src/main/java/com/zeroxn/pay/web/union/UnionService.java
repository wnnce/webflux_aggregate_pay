package com.zeroxn.pay.web.union;

import com.zeroxn.pay.core.PayTemplate;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;
import com.zeroxn.pay.core.exception.PayServiceException;
import com.zeroxn.pay.core.mq.PayMQTemplate;
import com.zeroxn.pay.module.union.UnionPayTemplate;
import com.zeroxn.pay.module.union.utils.UnionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/6/6 下午7:27
 * @Description: 云闪付支付Service层
 */
@Service
@ConditionalOnBean(UnionPayTemplate.class)
public class UnionService {
    private static final Logger logger = LoggerFactory.getLogger(UnionService.class);
    private final PayTemplate payTemplate;
    private final PayMQTemplate mqTemplate;
    public UnionService(@Qualifier("unionPayTemplate") PayTemplate payTemplate, PayMQTemplate mqTemplate){
        this.payTemplate = payTemplate;
        this.mqTemplate = mqTemplate;
    }

    /**
     * 云闪付WAP下单，返回URL编码后的支付表单，引号在被序列化时会被加上反斜杠 使用URL编码解决
     * @param paramDTO 支付参数
     * @return 被URL编码后的支付表单
     */
    public String unionWapPay(UnionParamDTO paramDTO){
        PayParams params = new PayParams.BuilderUnionWap()
                .setOrderId(paramDTO.getOrderId())
                .setTotal(paramDTO.getTotal())
                .setFrontUrl(paramDTO.getFrontUrl())
                .setDescription(paramDTO.getDescription())
                .build();
        String result = payTemplate.confirmOrder(params, PayMethod.WAP, String.class);
        UnionService.logger.info("云闪付生成支付参数成功，订单号：{}", params.getOrderId());
        result = URLEncoder.encode(result, StandardCharsets.UTF_8);
        return result.replaceAll("\\+", "%20");
    }

    /**
     * 通过商户系统内的订单号查询云闪付支付信息
     * @param orderId 商户系统内的订单ID
     * @return 云闪付系统内的支付信息 key-value形式
     */
    public Map<String, String> queryUnionOrder(String orderId){
        String result = payTemplate.queryOrder(orderId, null, String.class);
        Map<String, String> map = UnionUtils.stringToMap(result, "&", "=");
        map.remove("signPubKeyCert");
        return map;
    }

    /**
     * 云闪付订单退货
     * @param paramDTO 订单退货参数
     * @return 返回云闪付系统的响应结果
     */
    public Map<String, String> unionOrderRefund(UnionParamDTO paramDTO) {
        if(paramDTO.getRefundTotal() > paramDTO.getTotal()){
            throw new PayServiceException("订单退款金额不能大于订单总金额");
        }
        PayParams params = new PayParams.BuilderRefund()
                .setOrderId(paramDTO.getOrderId())
                .setRefundTotal(paramDTO.getRefundTotal())
                .setOrderRefundId(paramDTO.getQueryId())
                .build();
        String result = payTemplate.refundOrder(params, String.class);
        Map<String, String> map = UnionUtils.stringToMap(result, "&", "=");
        map.remove("signPubKeyCert");
        return map;
    }

    /**
     * 云闪付撤销订单
     * @param orderId 需要撤销的订单ID
     * @return 返回云闪付系统的响应结果
     */
    public Map<String, String> unionOrderRevoke(String orderId){
        String result = payTemplate.closeOrder(orderId, null, String.class);
        Map<String, String> map = UnionUtils.stringToMap(result, "&", "=");
        map.remove("signPubKeyCert");
        return map;
    }

    /**
     * 处理云闪付异步通知
     * @param paramsMap 通知的请求数据
     * @return 验签成功并且
     */
    public boolean unionSuccessNotify(Map<String, String> paramsMap){
        try{
            boolean result = UnionUtils.validateSign(paramsMap);
            if(result){
                String orderId = paramsMap.get("orderId");
                String resCode = paramsMap.get("respCode");
                if("00".equals(resCode) || "A6".equals(resCode)){
                    logger.info("校验异步通知参数成功，订单号：{}", orderId);
                    mqTemplate.send(PayPlatform.UNION, PayResult.SUCCESS, orderId);
                } else {
                    logger.warn("异步通知验签成功，数据校验失败，订单号：{}，响应码：{}", orderId, resCode);
                }
                return true;
            }
        }catch (Exception ex){
            logger.error("支付成功通知验签失败，错误消息：{}", ex.getMessage());
        }
        return false;
    }
    public boolean unionRefundNotify(Map<String, String> paramsMap){
        try{
            boolean result = UnionUtils.validateSign(paramsMap);
            if(result){
                String orderId = paramsMap.get("orderId");
                String resCode = paramsMap.get("respCode");
                //TODO 退款成功异步通知的应答码校验
                logger.info("退款成功通知验签成功，订单ID：{}，应答码：{}", orderId, resCode);
                mqTemplate.send(PayPlatform.UNION, PayResult.REFUND, orderId);
            }
        }catch (Exception ex){
            logger.error("退款成功通知验签失败，错误消息：{}", ex.getMessage());
        }
        return false;
    }
}