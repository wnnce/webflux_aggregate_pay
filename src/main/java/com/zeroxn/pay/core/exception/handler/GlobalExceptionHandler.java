package com.zeroxn.pay.core.exception.handler;

import com.zeroxn.pay.core.entity.Result;
import com.zeroxn.pay.core.enums.ResultCode;
import com.zeroxn.pay.module.alipay.exception.AlipayPayBusinessException;
import com.zeroxn.pay.module.alipay.exception.AlipayPaySystemException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Author: lisang
 * @DateTime: 2023/5/20 下午3:00
 * @Description: 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlipayPayBusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Result<String>> handlerAlipayPayBusinessException(@NotNull AlipayPayBusinessException ex){
        String message = ex.getMessage();
        return Mono.just(Result.field(ResultCode.REQUEST_FIELD, message));
    }
    @ExceptionHandler(AlipayPaySystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<Result<String>> handlerAlipayPaySystemException(@NotNull AlipayPaySystemException ex){
        String message = ex.getMessage();
        return Mono.just(Result.field(ResultCode.SYSTEM_FIELD, message));
    }
    /**
     * 处理validation参数校验错误
     * @param ex 参数校验错误异常
     * @return 返回错误消息
     */
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Result<String>> handlerMethodArgumentNotValidException(WebExchangeBindException ex){
        List<ObjectError> allErrors = ex.getAllErrors();
        String[] messages = allErrors.stream().map(ObjectError::getDefaultMessage).toArray(String[]::new);
        String errorMessage = String.join(",", messages);
        return Mono.just(Result.field(ResultCode.REQUEST_FIELD, errorMessage));
    }
}
