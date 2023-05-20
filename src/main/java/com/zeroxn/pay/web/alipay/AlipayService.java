package com.zeroxn.pay.web.alipay;

import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.response.*;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.module.alipay.exception.AlipayPayBusinessException;
import com.zeroxn.pay.module.alipay.exception.AlipayPaySystemException;
import com.zeroxn.pay.module.alipay.AlipayPayTemplate;
import com.zeroxn.pay.module.alipay.config.AlipayPayConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/5/18 下午7:46
 * @Description:
 */
@Service
@ConditionalOnBean(AlipayPayTemplate.class)
public class AlipayService {
    private final Logger logger = LoggerFactory.getLogger(AlipayService.class);
    /**
     * 支付宝业务错误码集合
     */
    private final Map<String, String> errorMap = Map.ofEntries(
            // Map.entry("ACQ.SYSTEM_ERROR", "系统错误"),
            Map.entry("ACQ.INVALID_PARAMETER", "请求参数无效"),
            // Map.entry("ACQ.ACCESS_FORBIDDEN", "调用接口无权限"),
            Map.entry("ACQ.EXIST_FORBIDDEN_WORD", "订单信息违规"),
            // Map.entry("ACQ.PARTNER_ERROR", "支付宝应用APP_ID填写错误"),
            Map.entry("ACQ.TOTAL_FEE_EXCEED", "订单金额错误"),
            Map.entry("ACQ.CONTEXT_INCONSISTENT", "订单已存在"),
            Map.entry("ACQ.TRADE_HAS_SUCCESS", "订单已存在"),
            Map.entry("ACQ.TRADE_HAS_CLOSE", "订单已存在"),
            Map.entry("ACQ.PAYMENT_REQUEST_HAS_RISK", "交易风险"),
            Map.entry("ACQ.RISK_MERCHANT_IP_NOT_EXIST", "需要IP信息"),
            Map.entry("ACQ.TRADE_NOT_EXIST", "交易不存在"),
            Map.entry("ACQ.TRADE_STATUS_ERROR", "交易状态异常"),
            Map.entry("ACQ.REFUND_AMT_NOT_EQUAL_TOTAL", "退款金额超限"),
            Map.entry("ACQ.ONLINE_TRADE_VOUCHER_NOT_ALLOW_REFUND", "交易不支持退款")
    );
    private final AlipayPayTemplate payHandler;
    private final AlipayPayConfig alipayConfig;
    public AlipayService(AlipayPayTemplate payHandler, AlipayPayConfig alipayConfig){
        this.payHandler = payHandler;
        this.alipayConfig = alipayConfig;
    }

    /**
     * 支付宝手机网站下单
     * @param paramDTO 下单参数
     * @return 返回手机下单所需的参数或空
     */
    public String alipayWapPay(AlipayParamDTO paramDTO){
        PayParams params = new PayParams.BuilderAliPayWap()
                .setOrderId(paramDTO.getOrderId())
                .setTotal(paramDTO.getTotal())
                .setDescription(paramDTO.getDescription())
                .setQuitUrl(paramDTO.getQuitUrl())
                .build();
        AlipayTradeWapPayResponse response = payHandler.confirmOrder(params, PayMethod.WAP, AlipayTradeWapPayResponse.class);
        if(response.isSuccess()){
            logger.info("支付宝手机下单成功，订单号：{}", params.getOrderId());
            return response.getBody();
        }
        responseValidation(response.getCode(), response.getSubCode(), response.getSubMsg(), params.getOrderId());
        return null;
    }

    /**
     * 支付宝电脑网站下单
     * @param paramDTO 下单参数
     * @return 电脑网站下单所需的参数或空
     */
    public String alipayDesktopPay(AlipayParamDTO paramDTO){
        if (paramDTO.getQrMode() != null && paramDTO.getQrMode() == 4){
            if (paramDTO.getQrWidth() == null){
                paramDTO.setQrWidth(50);
            }
        }
        PayParams params = new PayParams.BuilderAlipayDesktop()
                .setOrderId(paramDTO.getOrderId())
                .setTotal(paramDTO.getTotal())
                .setDescription(paramDTO.getDescription())
                .setQrMode(paramDTO.getQrMode().toString())
                .setQrWidth(Long.valueOf(paramDTO.getQrWidth()))
                .build();
        AlipayTradePagePayResponse response = payHandler.confirmOrder(params, PayMethod.DESKTOP, AlipayTradePagePayResponse.class);
        if (response.isSuccess()){
            logger.info("支付宝电脑下单成功，订单号：{}", params.getOrderId());
            return response.getBody();
        }
        responseValidation(response.getCode(), response.getSubCode(), response.getSubMsg(), params.getOrderId());
        return null;
    }

    /**
     * 支付宝小程序下单
     * @param paramDTO 下单参数
     * @return 小程序下单所需的参数或空
     */
    public String alipayAppletsPay(AlipayParamDTO paramDTO){
        PayParams params = new PayParams.BuilderAliPayApplets()
                .setOrderId(paramDTO.getOrderId())
                .setTotal(paramDTO.getTotal())
                .setDescription(paramDTO.getDescription())
                .setUserId(paramDTO.getUserId())
                .build();
        AlipayTradeCreateResponse response = payHandler.confirmOrder(params, PayMethod.APPLETS, AlipayTradeCreateResponse.class);
        if (response.isSuccess()){
            logger.info("支付宝小程序下单成功，订单号：{}", params.getOrderId());
            return response.getBody();
        }
        responseValidation(response.getCode(), response.getSubCode(), response.getSubMsg(), params.getOrderId());
        return null;
    }

    /**
     * 通过商户系统内的订单ID查询支付宝订单详情
     * @param orderId 订单id
     * @return 返回订单详细信息或空
     */
    public AlipayTradeQueryResponse queryAlipayOrder(String orderId){
        AlipayTradeQueryResponse response = payHandler.queryOrder(orderId, null, AlipayTradeQueryResponse.class);
        if(response.isSuccess()){
            return response;
        }
        logger.warn("支付宝订单查询失败");
        responseValidation(response.getCode(), response.getSubCode(), response.getSubMsg(), orderId);
        return null;
    }

    /**
     * 通过商户系统内的订单ID关闭支付宝订单
     * @param orderId 订单id
     * @return 关闭成功则返回订单id否则空
     */
    public String alipayOrderClose(String orderId){
        AlipayTradeCloseResponse response = payHandler.closeOrder(orderId, null, AlipayTradeCloseResponse.class);
        if(response.isSuccess()){
            return response.getOutTradeNo();
        }
        logger.warn("支付宝订单关闭失败");
        responseValidation(response.getCode(), response.getSubCode(), response.getSubMsg(), orderId);
        return null;
    }

    /**
     * 支付宝订单退款 退款金额不能大于订单总金额 需要部分退款时 refundId必须传入
     * @param paramDTO 退款参数
     * @return 退款成功则返回退款订单详情否则返回空
     */
    public AlipayTradeRefundResponse alipayOrderRefund(AlipayParamDTO paramDTO){
        if(paramDTO.getRefundTotal() > paramDTO.getTotal()){
            throw new AlipayPayBusinessException("退款金额不能大于订单总金额");
        }
        if(paramDTO.getRefundTotal() < paramDTO.getTotal() && StringUtils.isEmpty(paramDTO.getRefundId())){
            throw new AlipayPayBusinessException("部分退款必须传入退款ID");
        }
        PayParams params = new PayParams.BuilderRefund()
                .setOrderId(paramDTO.getOrderId())
                .setOrderRefundId(paramDTO.getRefundId())
                .setTotal(paramDTO.getTotal())
                .setRefundTotal(paramDTO.getRefundTotal())
                .setRefundDescription(paramDTO.getRefundDescription())
                .build();
        AlipayTradeRefundResponse response = payHandler.refundOrder(params, AlipayTradeRefundResponse.class);
        if(response.isSuccess()){
            return response;
        }
        logger.warn("支付宝订单退款失败");
        responseValidation(response.getCode(), response.getSubCode(), response.getSubMsg(), params.getOrderId());
        return null;
    }

    /**
     * 支付宝通过订单id和订单退款id查询退款订单详情 如果是推全款没有订单退款id 那么两个参数都是订单id
     * @param orderId 订单id
     * @param refundId 订单退款id 没有则为订单id
     * @return 返回退款订单详情或空
     */
    public AlipayTradeFastpayRefundQueryResponse queryAlipayRefundOrder(String orderId, String refundId){
        AlipayTradeFastpayRefundQueryResponse response = payHandler.queryRefundOrder(orderId, refundId, AlipayTradeFastpayRefundQueryResponse.class);
        if(response.isSuccess()){
            return response;
        }
        logger.warn("支付宝退款订单查询失败");
        responseValidation(response.getCode(), response.getSubCode(), response.getSubMsg(), orderId);
        return null;
    }

    /**
     * 支付宝异步通知结果签名校验
     * @param paramsMap 通知参数
     * @return 返回 success 或者 field
     */
    public String alipayNotifyVerified(Map<String, String> paramsMap){
        if(payHandler.notifySignVerified(paramsMap)){
            String orderId = paramsMap.get("out_trade_no");
            AlipayTradeQueryResponse response = queryAlipayOrder(orderId);
            if (response != null && response.getTotalAmount().equals(paramsMap.get("total_amount"))){
                if(alipayConfig.getAppId().equals(paramsMap.get("app_id"))){
                    // 将通知数据放入消息队列


                    return "success";
                }
            }
        }
        return "field";
    }

    private void responseValidation(@NotNull String code, @NotNull String subCode, @NotNull String subMsg, String orderId){
        if(code.equals("40004")){
            handlerResponse40004(subCode, subMsg, orderId);
        }
        logger.error("支付宝接口调用出现其他错误，错误码：{}，错误返回码：{}，错误消息：{}", code, subCode, subMsg);
        throw new AlipayPaySystemException("系统错误，请重试");
    }
    private void handlerResponse40004(String subCode, String subMsg, String orderId){
        if (errorMap.containsKey(subCode)){
            String errorMessage = errorMap.get(subCode);
            logger.warn("支付宝交易业务异常，错误码：{}，错误消息：{}，订单号：{}", subCode, subMsg, orderId);
            throw new AlipayPayBusinessException(errorMessage);
        }
        logger.error("支付宝交易系统异常，错误码：{}，错误消息：{}，订单号：{}", subCode, subMsg, orderId);
        throw new AlipayPaySystemException("系统错误，请重试");
    }
}
