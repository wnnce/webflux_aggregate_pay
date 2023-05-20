package com.zeroxn.pay.web.alipay;

import com.zeroxn.pay.core.entity.Result;
import com.zeroxn.pay.core.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @Author: lisang
 * @DateTime: 2023/5/18 下午3:06
 * @Description: 支付宝支付接口
 */
@RestController
@RequestMapping("/pay/alipay")
@Tag(name = "支付宝支付管理接口")
public class AlipayController {
    private final AlipayService alipayService;
    public AlipayController(AlipayService alipayService){
        this.alipayService = alipayService;
    }

    @PostMapping("/wap")
    @Operation(summary = "支付宝手机下单接口")
    public Mono<Result<String>> alipayWap(@RequestBody @Validated(ValidationGroups.AlipayWapValidation.class)
                                                   AlipayParamDTO paramDTO){
        String data = alipayService.alipayWapPay(paramDTO);
        return Mono.just(Result.success(data));
    }
    @PostMapping("/desktop")
    @Operation(summary = "支付宝电脑网站下单接口")
    public Mono<Result<String>> alipayDesktop(@RequestBody @Validated(ValidationGroups.AlipayDesktopValidation.class)
                                                       AlipayParamDTO paramDTO){
        String data = alipayService.alipayDesktopPay(paramDTO);
        return Mono.just(Result.success(data));
    }
    @PostMapping("/applets")
    @Operation(summary = "支付宝小程序下单接口")
    public Mono<Result<String>> alipayApplets(@RequestBody @Validated(ValidationGroups.AlipayAppletsValidation.class)
                                                       AlipayParamDTO paramDTO){
        String data = alipayService.alipayAppletsPay(paramDTO);
        return Mono.just(Result.success(data));
    }
    @GetMapping("/{id}")
    @Operation(summary = "支付宝订单查询接口")
    @Parameter(name = "id", description = "商户系统内的订单ID", required = true)
    public Mono<Result<String>> queryAlipayOrder(@PathVariable("id") String orderId){
        return null;
    }
    @PostMapping("/close/{id}")
    @Operation(summary = "支付宝订单关闭接口")
    @Parameter(name = "id", description = "商户系统内的订单ID", required = true)
    public Mono<Result<String>> closeAlipayOrder(@PathVariable("id") String orderId){
        return null;
    }
    @PostMapping("/refund")
    @Operation(summary = "支付宝订单退款接口")
    public Mono<Result<String>> alipayOrderRefund(AlipayParamDTO paramDTO){
        return null;
    }

    @GetMapping("/refund/{orderId}/{refundId}")
    @Operation(summary = "支付宝订单退款查询接口")
    @Parameter(name = "orderId", description = "商户系统内的订单ID", required = true)
    @Parameter(name = "refundId", description = "商户系统内的订单退款ID", required = true)
    public Mono<Result<String>> queryAlipayRefundOrder(@PathVariable("orderId") String orderId,
                                                       @PathVariable("refundId") String refundId){
        return null;
    }
}
