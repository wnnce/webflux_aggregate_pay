package com.zeroxn.pay.core.entity;

import com.zeroxn.pay.core.enums.ResultCode;
import org.jetbrains.annotations.NotNull;

/**
 * @Author: lisang
 * @DateTime: 2023/5/19 下午1:53
 * @Description: 响应消息实体类
 */
public class Result<T> {
    private int code;
    private String message;
    private long timestamp;
    private T data;
    public Result() {
    }

    public Result(int code, String message, long timestamp, T data) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.data = data;
    }

    public Result(int code, String message, long timestamp) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * 操作成功的响应方法
     * @param data 响应数据
     * @return 响应体
     * @param <T> 泛型 任意类型
     */
    @NotNull
    public static <T> Result<T> success(T data){
        return new Result<>(ResultCode.SUCCESS.getCode(), "success", System.currentTimeMillis(), data);
    }

    /**
     * 操作失败的响应方法
     * @param code 错误码
     * @param message 错误消息
     * @return 响应体
     * @param <T> 泛型
     */
    public static <T> Result<T> field(@NotNull ResultCode code, String message){
        return new Result<>(code.getCode(), message, System.currentTimeMillis());
    }

    /**
     * 操作失败的响应方法
     * @param code 错误码
     * @param message 错误消息
     * @param data 错误详情
     * @return 响应体
     * @param <T> 泛型
     */
    public static <T> Result<T> field(ResultCode code, String message, T data){
        return new Result<>(code.getCode(), message, System.currentTimeMillis(), data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
