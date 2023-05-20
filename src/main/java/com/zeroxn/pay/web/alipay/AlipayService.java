package com.zeroxn.pay.web.alipay;

import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.core.exception.AlipayPayException;
import com.zeroxn.pay.module.alipay.AlipayPayTemplate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: lisang
 * @DateTime: 2023/5/18 下午7:46
 * @Description:
 */
@Service
public class AlipayService {
    private final Logger logger = LoggerFactory.getLogger(AlipayService.class);
    private final AlipayPayTemplate payHandler;
    public AlipayService(AlipayPayTemplate payHandler){
        this.payHandler = payHandler;
    }
    public String alipayWapPay(AlipayParamDTO paramDTO){
        PayParams params = new PayParams.BuilderAliPayWap()
                .setOrderId(paramDTO.getOrderId())
                .setTotal(paramDTO.getTotal())
                .setDescription(paramDTO.getDescription())
                .setQuitUrl(paramDTO.getQuitUrl())
                .build();
        AlipayTradeWapPayResponse response = payHandler.handlerConfirmOrder(params, PayMethod.WAP, AlipayTradeWapPayResponse.class);
        if(response.isSuccess()){
            logger.info("支付宝手机下单成功，订单号：{}", params.getOrderId());
            return response.getBody();
        }else {
            responseValidation(response.getCode(), response.getSubCode(), response.getSubMsg(), params.getOrderId());
        }
        return null;
    }
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
        AlipayTradePagePayResponse response = payHandler.handlerConfirmOrder(params, PayMethod.DESKTOP, AlipayTradePagePayResponse.class);
        if (response.isSuccess()){
            logger.info("支付宝电脑下单成功，订单号：{}", params.getOrderId());
            return response.getBody();
        }else {
            responseValidation(response.getCode(), response.getSubCode(), response.getSubMsg(), params.getOrderId());
        }
        return null;
    }
    public String alipayAppletsPay(AlipayParamDTO paramDTO){
        PayParams params = new PayParams.BuilderAliPayApplets()
                .setOrderId(paramDTO.getOrderId())
                .setTotal(paramDTO.getTotal())
                .setDescription(paramDTO.getDescription())
                .setUserId(paramDTO.getUserId())
                .build();
        AlipayTradeCreateResponse response = payHandler.handlerConfirmOrder(params, PayMethod.APPLETS, AlipayTradeCreateResponse.class);
        if (response.isSuccess()){
            logger.info("支付宝小程序下单成功，订单号：{}", params.getOrderId());
            return response.getBody();
        }else {
            responseValidation(response.getCode(), response.getSubCode(), response.getSubMsg(), params.getOrderId());
        }
        return null;
    }
    private void responseValidation(@NotNull String code, @NotNull String subCode, @NotNull String subMsg, String orderId){
        if(code.equals("40004")){
            handlerResponse40004(subCode, subMsg, orderId);
        }
        logger.error("支付宝接口调用出现其他错误，错误码：{}，错误返回码：{}，错误消息：{}", code, subCode, subMsg);
        throw new AlipayPayException("系统错误，请重试");
    }
    private void handlerResponse40004(String subCode, String subMsg, String orderId){
        switch (subCode){
            case "ACQ.SYSTEM_ERROR" -> {
                logger.error("支付宝手机下单接口错误");
                throw new AlipayPayException("系统错误");
            }
            case "ACQ.INVALID_PARAMETER" -> {
                logger.warn("支付宝手机下单请求参数无效");
                throw new AlipayPayException("请求参数无效");
            }
            case "ACQ.ACCESS_FORBIDDEN" -> {
                logger.error("调用接口无权限");
                throw new AlipayPayException("系统错误");
            }
            case "ACQ.EXIST_FORBIDDEN_WORD" -> {
                logger.error("手机下单订单信息违规");
                throw new AlipayPayException("订单信息违规");
            }
            case "ACQ.PARTNER_ERROR" -> {
                logger.error("支付宝应用APP_ID填写错误");
                throw new AlipayPayException("系统错误");
            }
            case "ACQ.TOTAL_FEE_EXCEED" -> {
                logger.warn("订单金额超过允许值");
                throw new AlipayPayException("订单金额错误");
            }
            case "ACQ.CONTEXT_INCONSISTENT" -> {
                logger.warn("交易信息被篡改，订单号：{}", orderId);
                throw new AlipayPayException("订单已存在");
            }
            case "ACQ.TRADE_HAS_SUCCESS" -> {
                logger.warn("该订单号已被支付，订单号：{}", orderId);
                throw new AlipayPayException("订单已存在");
            }
            case "ACQ.TRADE_HAS_CLOSE" -> {
                logger.warn("该订单号已被关闭，订单号：{}", orderId);
                throw new AlipayPayException("订单已存在");
            }
            case "ACQ.PAYMENT_REQUEST_HAS_RISK" -> {
                logger.error("交易存在风险");
                throw new AlipayPayException("交易风险");
            }
            case "ACQ.RISK_MERCHANT_IP_NOT_EXIST" -> {
                logger.warn("订单未传入IP信息");
                throw new AlipayPayException("需要IP信息");
            }
            default -> {
                logger.error("支付宝支付其他错误，错误码：{}，错误消息：{}，订单号：{}", subCode, subMsg, orderId);
                throw new AlipayPayException("其他错误，请重试");
            }
        }
    }
}
