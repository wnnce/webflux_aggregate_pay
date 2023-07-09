package com.zeroxn.pay.module.wechat.handler;

import com.wechat.pay.java.core.exception.ServiceException;
import com.zeroxn.pay.module.wechat.utils.WechatUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午4:21
 * @Description: 处理微信支付异常
 */
@RestControllerAdvice
@ConditionalOnProperty(value = "pay.wechat.enable", havingValue = "true")
public class WechatExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public void handlerServiceException(ServiceException ex) {
        WechatUtils.serviceExceptionHandler(ex);
    }
}
