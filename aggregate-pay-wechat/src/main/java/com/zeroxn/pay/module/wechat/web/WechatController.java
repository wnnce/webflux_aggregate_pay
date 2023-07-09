package com.zeroxn.pay.module.wechat.web;

import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.Refund;
import com.zeroxn.pay.core.entity.Result;
import com.zeroxn.pay.core.validation.ValidationGroups;
import com.zeroxn.pay.module.wechat.WechatPayTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @Author: lisang
 * @DateTime: 2023/5/20 下午6:25
 * @Description: 微信支付Controller层
 */
@RestController
@RequestMapping("/pay/wechat")
@Tag(name = "微信支付接口")
@ConditionalOnBean(WechatPayTemplate.class)
public class WechatController {
    private final WechatService wechatService;
    public WechatController(WechatService wechatService){
        this.wechatService = wechatService;
    }

    /**
     * 微信支付H5平台下单
     * @param paramDTO 下单参数
     * @return H5下单需要的跳转url
     */
    @PostMapping("/h5")
    @Operation(summary = "微信支付H5下单接口")
    public Mono<Result<String>> wechatH5(@RequestBody @Validated(ValidationGroups.WechatH5Validation.class) WechatParamDTO paramDTO){
        String result = wechatService.wechatH5Pay(paramDTO);
        return Mono.just(Result.success(result));
    }

    /**
     * 微信平台jsapi下单（小程序）
     * @param paramDTO 下单参数
     * @return 返回小程序下单所需的参数
     */
    @PostMapping("/jsapi")
    @Operation(summary = "微信支付Jsapi下单接口（小程序）")
    public Mono<Result<PrepayWithRequestPaymentResponse>> wechatJsapi(@RequestBody @Validated(ValidationGroups.WechatAppletsValidation.class)
                                                                          WechatParamDTO paramDTO){
        PrepayWithRequestPaymentResponse response = wechatService.wechatJsapiPay(paramDTO);
        return Mono.just(Result.success(response));
    }

    /**
     * 查询微信支付订单
     * @param method 订单的下单方式 1：小程序;2：H5
     * @param orderId 商户系统内的订单ID
     * @return 返回订单详细信息
     */
    @GetMapping("/{method}/{id}")
    @Operation(summary = "微信支付订单查询接口")
    @Parameter(name = "method", description = "该订单的下单方式，1：小程序;2：H5", required = true)
    @Parameter(name = "id", description = "商户系统内的订单ID", required = true)
    public Mono<Result<Transaction>> queryWechatOrder(@PathVariable("method") Integer method, @PathVariable("id") String orderId){
        Transaction transaction = wechatService.queryWechatOrder(method, orderId);
        return Mono.just(Result.success(transaction));
    }

    /**
     * 微信支付关闭订单
     * @param method 订单的下单方式 1：小程序;2：H5
     * @param orderId 商户系统内的订单ID
     * @return 关闭成功返回订单ID
     */
    @PostMapping("/close/{method}/{id}")
    @Operation(summary = "微信支付订单关闭接口")
    @Parameter(name = "method", description = "该订单的下单方式，1：小程序;2：H5", required = true)
    @Parameter(name = "id", description = "商户系统内的订单ID", required = true)
    public Mono<Result<String>> closeWechatOrder(@PathVariable("method") Integer method, @PathVariable("id") String orderId){
        String result = wechatService.wechatOrderClose(method, orderId);
        return Mono.just(Result.success(result));
    }

    /**
     * 微信支付订单退款
     * @param paramDTO 下单参数
     * @return 返回退款订单详情
     */
    @PostMapping("/refund")
    @Operation(summary = "微信支付订单退款接口")
    public Mono<Result<Refund>> wechatRefund(@RequestBody @Validated(ValidationGroups.WechatRefundValidation.class)
                                                 WechatParamDTO paramDTO){

        Refund refund = wechatService.wechatOrderRefund(paramDTO);
        return Mono.just(Result.success(refund));
    }

    /**
     * 查询微信支付退款订单
     * @param refundId 商户系统内的订单退款ID
     * @return 返回退款订单详情
     */
    @PostMapping("/refund/{id}")
    @Operation(summary = "微信支付查询退款订单接口")
    @Parameter(name = "id", description = "商户系统内的订单退款单号", required = true)
    public Mono<Result<Refund>> queryWechatRefundOrder(@PathVariable("id") String refundId){
        Refund refund = wechatService.queryWechatRefundOrder(refundId);
        return Mono.just(Result.success(refund));
    }
    /**
     * 接收支付成功后微信服务器发送的回调通知
     * @param requestBody 请求体
     * @param signTure 请求头
     * @param nonce 请求头 随机字符串
     * @param timestamp 请求头 时间戳
     * @param serial 请求头 解密需要的参数
     * @param signType 请求头 加密方式
     */
    @PostMapping("/notify/success")
    public ResponseEntity<NotifyResult> wechatSuccessNotify(@RequestBody String requestBody,
                                                            @RequestHeader("Wechatpay-Signature") String signTure,
                                                            @RequestHeader("Wechatpay-Nonce") String nonce,
                                                            @RequestHeader("Wechatpay-Timestamp") String timestamp,
                                                            @RequestHeader("Wechatpay-Serial") String serial,
                                                            @RequestHeader("Wechatpay-Signature-Type") String signType){
        boolean result = wechatService.wechatSuccessNotify(signTure, signType, nonce, timestamp, serial, requestBody);
        ResponseEntity<NotifyResult> entity;
        if (result){
            entity = new ResponseEntity<>(HttpStatus.OK);
        }else {
            NotifyResult notifyResult = new NotifyResult("FIELD", "失败");
            entity = new ResponseEntity<>(notifyResult, HttpStatus.PAYMENT_REQUIRED);
        }
        return entity;
    }

    /**
     * 接收微信支付退款后的回调通知
     * @param requestBody 请求体
     * @param signTure 请求头
     * @param nonce 请求头 随机字符串
     * @param timestamp 请求头 时间戳
     * @param serial 请求头 解密需要的参数
     * @param signType 请求头 加密方式
     */
    @PostMapping("/notify/refund")
    public ResponseEntity<NotifyResult> wechatRefundNotify(@RequestBody String requestBody,
                                                           @RequestHeader("Wechatpay-Signature") String signTure,
                                                           @RequestHeader("Wechatpay-Nonce") String nonce,
                                                           @RequestHeader("Wechatpay-Timestamp") String timestamp,
                                                           @RequestHeader("Wechatpay-Serial") String serial,
                                                           @RequestHeader("Wechatpay-Signature-Type") String signType) {
        boolean result = wechatService.wechatRefundNotify(signTure, signType, nonce, timestamp, serial, requestBody);
        ResponseEntity<NotifyResult> entity;
        if (result){
            entity = new ResponseEntity<>(HttpStatus.OK);
        }else {
            NotifyResult notifyResult = new NotifyResult("FIELD", null);
            entity = new ResponseEntity<>(notifyResult, HttpStatus.PAYMENT_REQUIRED);
        }
        return entity;
    }
    record NotifyResult(String Code, String message){};
}
