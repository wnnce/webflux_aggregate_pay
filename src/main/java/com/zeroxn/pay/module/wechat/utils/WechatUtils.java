package com.zeroxn.pay.module.wechat.utils;

import com.wechat.pay.java.core.exception.ServiceException;
import com.zeroxn.pay.core.exception.PayServiceException;
import com.zeroxn.pay.core.exception.PaySystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/5/23 下午3:14
 * @Description: 微信支付工具类
 */
public class WechatUtils {
    private static final Logger logger = LoggerFactory.getLogger(WechatUtils.class);
    private static final Map<String, String> errorMap = Map.ofEntries(
            Map.entry("PARAM_ERROR", "参数错误"),
            Map.entry("OUT_TRADE_NO_USED", "订单号重复"),
            Map.entry("ORDER_NOT_EXIST", "订单不存在"),
            Map.entry("ORDER_CLOSED", "订单已被关闭"),
            Map.entry("FREQUENCY_LIMITED", "请求过于频繁"),
            Map.entry("INVALID_TRANSACTIONID", "订单号无效"),
            Map.entry("INVALID_REQUEST", "请求无效"),
            Map.entry("TRADE_ERROR", "交易错误"),
            Map.entry("NOTENOUGH", "余额不足"),
            Map.entry("ORDERNOTEXIST", "订单不存在"),
            Map.entry("USER_ACCOUNT_ABNORMAL", "退款请求失败"),
            Map.entry("NOT_ENOUGH", "余额不足"),
            Map.entry("NO_AUTH", "没有退款权限"),
            Map.entry("RESOURCE_NOT_EXISTS", "退款单查询失败")
    );
    public static void serviceExceptionHandler(ServiceException ex){
        int statusCode = ex.getHttpStatusCode();
        String errorCode = ex.getErrorCode();
        String errorMessage = ex.getErrorMessage();
        WechatUtils.logger.error("微信支付接口调用异常，状态码：{}，错误码：{}，错误消息：{}", statusCode, errorCode, errorMessage);
        if (errorMap.containsKey(errorCode)){
            String message = errorMap.get(errorCode);
            throw new PayServiceException(message);
        }else {
            throw new PaySystemException("微信支付系统错误，请重试");
        }
    }
}
