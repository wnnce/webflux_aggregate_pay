package com.zeroxn.pay.module.jdpay.web;

import com.zeroxn.pay.core.entity.Result;
import com.zeroxn.pay.module.jdpay.config.ConditionalOnJdPay;
import com.zeroxn.pay.module.jdpay.validation.JdPayValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Conditional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023-09-11 10:29:18
 * @Description: 京东支付Controller层
 */
@RestController
@RequestMapping("/pay/jd")
@Tag(name = "京东支付接口")
@Conditional(ConditionalOnJdPay.class)
public class JdPayController {
    private final JdPayService service;
    public JdPayController(JdPayService service) {
        this.service = service;
    }
    @PostMapping("/order")
    @Operation(description = "京东支付统一下单接口")
    public Mono<Result<Map<String, Object>>> jdPayConfirmOrder(@RequestBody @Validated(JdPayValidation.ConfirmValidation.class) JdPayParamsDto params) {
        Map<String, Object> result = service.jdPayConfirmOrder(params);
        return Mono.just(Result.success(result));
    }
    @GetMapping("/{id}")
    @Operation(description = "京东支付查询订单")
    @Parameter(name = "id", description = "下单时的订单ID", required = true)
    public Mono<Result<Map<String, Object>>> jdPayQueryOrder(@PathVariable("id") String orderId) {
        Map<String, Object> result = service.jdPayQueryOrder(orderId);
        return Mono.just(Result.success(result));
    }

    @GetMapping("/{orderId}/{refundId}")
    @Operation(description = "京东支付查询退款订单")
    @Parameter(name = "orderId", description = "下单时的订单ID", required = true)
    @Parameter(name = "refundId", description = "订单申请退款时的退款流水号", required = true)
    public Mono<Result<Map<String, Object>>> jdPayQueryRefundOrder(@PathVariable("orderId") String orderId, @PathVariable("refundId") String refundId) {
        Map<String, Object> result = service.jdPayQueryRefundOrder(orderId, refundId);
        return Mono.just(Result.success(result));
    }

    @PostMapping("/close")
    @Operation(description = "关闭京东支付订单")
    public Mono<Result<Map<String, Object>>> jdPayCloseOrder(@RequestBody @Validated(JdPayValidation.RefundValidation.class) JdPayParamsDto paramsDto) {
        Map<String, Object> result = service.jdPayCloseOrder(paramsDto);
        return Mono.just(Result.success(result));
    }

    @PostMapping("/refund")
    @Operation(description = "京东支付订单退款")
    public Mono<Result<Map<String, Object>>> jdPayRefundOrder(@RequestBody @Validated(JdPayValidation.RefundValidation.class) JdPayParamsDto paramsDto) {
        Map<String, Object> result = service.jdPayRefundOrder(paramsDto);
        return Mono.just(Result.success(result));
    }

    @PostMapping("/notify/success")
    @Operation(description = "京东支付成功异步通知接口")
    public Mono<String> jdPaySuccessNotify(@RequestBody Map<String, Object> params) {
        String result = service.jdPaySuccessNotify(params);
        return Mono.just(result);
    }

    @PostMapping("/notify/refund")
    @Operation(description = "京东支付退款成功异步通知接口")
    public Mono<String> jdPayRefundNotify(@RequestBody Map<String, Object> params) {
        String result = service.jdPayRefundNotify(params);
        return Mono.just(result);
    }
}
