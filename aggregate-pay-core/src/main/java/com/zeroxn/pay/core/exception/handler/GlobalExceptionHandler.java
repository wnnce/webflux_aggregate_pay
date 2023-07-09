package com.zeroxn.pay.core.exception.handler;

import com.zeroxn.pay.core.entity.Result;
import com.zeroxn.pay.core.enums.ResultCode;
import com.zeroxn.pay.core.exception.PayServiceException;
import com.zeroxn.pay.core.exception.PaySystemException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 处理支付业务异常
     * @param ex 支付业务异常
     * @return 返回错误消息和400状态码
     */
    @ExceptionHandler(PayServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Result<String>> handlerAlipayPayBusinessException(@NotNull PayServiceException ex){
        String message = ex.getMessage();
        return Mono.just(Result.field(ResultCode.REQUEST_FIELD, message));
    }

    /**
     * 处理支付系统异常
     * @param ex 支付系统异常
     * @return 返回错误消息和500状态码
     */
    @ExceptionHandler(PaySystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<Result<String>> handlerAlipayPaySystemException(@NotNull PaySystemException ex){
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
    public Mono<Result<String>> handlerMethodArgumentNotValidException(@NotNull WebExchangeBindException ex){
        List<ObjectError> allErrors = ex.getAllErrors();
        String[] messages = allErrors.stream().map(ObjectError::getDefaultMessage).toArray(String[]::new);
        String errorMessage = String.join(",", messages);
        return Mono.just(Result.field(ResultCode.REQUEST_FIELD, errorMessage));
    }
    @ExceptionHandler(Exception.class)
    public Mono<Result<String>> handlerException(Exception ex){
        GlobalExceptionHandler.logger.error("系统错误，错误消息：{}", ex.getMessage());
        return Mono.just(Result.field(ResultCode.SYSTEM_FIELD, "系统错误"));
    }
}
