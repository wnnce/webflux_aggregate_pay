package com.zeroxn.pay.web.wechat;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @Author: lisang
 * @DateTime: 2023/5/20 下午6:25
 * @Description:
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
    @PostMapping("/h5")
    @Operation(summary = "微信支付H5下单接口")
    public Mono<Result<String>> wechatH5(@RequestBody @Validated(ValidationGroups.WechatH5Validation.class) WechatParamDTO paramDTO){
        String result = wechatService.wechatH5Pay(paramDTO);
        return Mono.just(Result.success(result));
    }
    @PostMapping("/jsapi")
    @Operation(summary = "微信支付Jsapi下单接口（小程序）")
    public Mono<Result<PrepayWithRequestPaymentResponse>> wechatJsapi(@RequestBody @Validated(ValidationGroups.WechatAppletsValidation.class)
                                                                          WechatParamDTO paramDTO){
        PrepayWithRequestPaymentResponse response = wechatService.wechatJsapiPay(paramDTO);
        return Mono.just(Result.success(response));
    }
    @GetMapping("/{method}/{id}")
    @Operation(summary = "微信支付订单查询接口")
    @Parameter(name = "method", description = "该订单的下单方式，1：小程序;2：H5", required = true)
    @Parameter(name = "id", description = "商户系统内的订单ID", required = true)
    public Mono<Result<Transaction>> queryWechatOrder(@PathVariable("method") Integer method, @PathVariable("id") String orderId){
        Transaction transaction = wechatService.queryWechatOrder(method, orderId);
        return Mono.just(Result.success(transaction));
    }
    @PostMapping("/close/{method}/{id}")
    @Operation(summary = "微信支付订单关闭接口")
    @Parameter(name = "method", description = "该订单的下单方式，1：小程序;2：H5", required = true)
    @Parameter(name = "id", description = "商户系统内的订单ID", required = true)
    public Mono<Result<String>> closeWechatOrder(@PathVariable("method") Integer method, @PathVariable("id") String orderId){
        String result = wechatService.wechatOrderClose(method, orderId);
        return Mono.just(Result.success(result));
    }
    @PostMapping("/refund")
    @Operation(summary = "微信支付订单退款接口")
    public Mono<Result<Refund>> wechatRefund(@RequestBody @Validated(ValidationGroups.WechatRefundValidation.class)
                                                 WechatParamDTO paramDTO){

        Refund refund = wechatService.wechatOrderRefund(paramDTO);
        return Mono.just(Result.success(refund));
    }
    @PostMapping("/refund/{id}")
    @Operation(summary = "微信支付查询退款订单接口")
    @Parameter(name = "id", description = "商户系统内的订单退款单号", required = true)
    public Mono<Result<Refund>> queryWechatRefundOrder(@PathVariable("id") String refundId){
        Refund refund = wechatService.queryWechatRefundOrder(refundId);
        return Mono.just(Result.success(refund));
    }
}
