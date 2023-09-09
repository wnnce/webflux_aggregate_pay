package com.zeroxn.pay.module.paypal.web;

import com.zeroxn.pay.core.entity.Result;
import com.zeroxn.pay.module.paypal.config.ConditionalOnPayPal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Conditional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @Author: lisang
 * @DateTime: 2023-09-08 13:01:58
 * @Description: Paypal Web控制层
 */
@RestController
@RequestMapping("/pay/paypal")
@Tag(name = "PayPal支付接口")
@Conditional(ConditionalOnPayPal.class)
public class PayPalController {

    private final PayPalService payPalService;

    public PayPalController(PayPalService payPalService){
        this.payPalService = payPalService;
    }

    /**
     * PayPal创建订单接口
     * @param params 创建订单的参数
     * @return 返回该订单的支付链接
     */
    @PostMapping("/order")
    @Operation(description = "PayPal创建订单接口")
    public Mono<Result<Object>> payPalCreateOrder(@RequestBody @Validated PayPalParamsDTO params) {
        Object result = payPalService.paypalCreateOrder(params);
        return Mono.just(Result.success(result));
    }

    /**
     * PayPal订单扣款接口，用户同意订单后会跳转到指定的地址并带上PayPal内部的订单ID，再调用扣款接口并传入该订单ID即可完成收款
     * @param orderId 用户同意扣款后PayPal生成的订单ID
     * @return 扣款结果
     */
    @PostMapping("/capture/{id}")
    @Operation(description = "PayPal订单扣款接口，用户同意订单后会跳转到指定的地址并带上PayPal内部的订单ID，再调用扣款接口并传入该订单ID即可完成收款")
    @Parameter(name = "id", description = "用户同意扣款后PayPal生成的订单ID", required = true)
    public Mono<Result<Object>> payPalConfirmOrder(@PathVariable("id") String orderId) {
        Object result = payPalService.paypalConfirmOrder(orderId);
        return Mono.just(Result.success(result));
    }

    /**
     * PayPal查询订单接口
     * @param orderId PayPal生成的订单ID
     * @return 订单详细参数
     */
    @GetMapping("/{id}")
    @Operation(description = "PayPal查询订单接口")
    @Parameter(name = "id", description = "PayPal生成的订单ID", required = true)
    public Mono<Result<Object>> payPalQueryOrder(@PathVariable("id") String orderId) {
        Object result = payPalService.queryPayPalOrder(orderId);
        return Mono.just(Result.success(result));
    }

    /**
     * PayPal订单退款接口
     * @param captureId PayPal生成的订单captureId，可以通过查询订单得到
     * @return 订单退款成功或失败
     */
    @PostMapping("/refund/{id}")
    @Operation(description = "PayPal订单退款接口")
    @Parameter(name = "id", description = "PayPal生成的订单captureId", required = true)
    public Mono<Result<Object>> payPalRefundOrder(@PathVariable("id") String captureId) {
        Object result = payPalService.paypalOrderRefund(captureId);
        return Mono.just(Result.success(result));
    }

    /**
     * PayPal查询退款订单接口
     * @param refundId PayPal的订单退款ID,在订单退款成功后会返回
     * @return 退款订单详情
     */
    @GetMapping("/refund/{id}")
    @Operation(description = "PayPal查询退款订单接口")
    @Parameter(name = "id", description = "PayPal退款时生成的订单退款ID", required = true)
    public Mono<Result<Object>> payPalQueryRefund(@PathVariable("id") String refundId) {
        Object result = payPalService.queryPayPalRefundOrder(refundId);
        return Mono.just(Result.success(result));
    }
}
