package com.zeroxn.pay.web.union;

import com.zeroxn.pay.core.entity.Result;
import com.zeroxn.pay.core.validation.ValidationGroups;
import com.zeroxn.pay.module.union.UnionPayTemplate;
import com.zeroxn.pay.module.union.utils.UnionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/6/6 下午7:27
 * @Description: 云闪付支付接口
 */
@RestController
@RequestMapping("/pay/union")
@Tag(name = "云闪付支付接口")
@ConditionalOnBean(UnionPayTemplate.class)
public class UnionController {
    private final UnionService unionService;
    public UnionController(UnionService unionService){
        this.unionService = unionService;
    }
    @PostMapping("/wap")
    @Operation(description = "云闪付手机下单接口")
    public Mono<Result<String>> unionWap(@RequestBody @Validated(ValidationGroups.UnionWapValidation.class) UnionParamDTO paramDTO){
        String result = unionService.unionWapPay(paramDTO);
        return Mono.just(Result.success(result));
    }
    @GetMapping("/{id}")
    @Operation(description = "云闪付订单查询接口")
    @Parameter(name = "id", description = "商户系统内的订单ID", required = true)
    public Mono<Result<Map<String, String>>> queryUnionOrder(@PathVariable("id") String orderId){
        Map<String, String> map = unionService.queryUnionOrder(orderId);
        return Mono.just(Result.success(map));
    }
    @GetMapping("/revoke/{id}")
    @Operation(description = "云闪付订单撤销接口")
    @Parameter(name = "id", description = "商户系统内的订单ID", required = true)
    public Mono<Result<Map<String, String>>> unionOrderRevoke(@PathVariable("id") String orderId){
        Map<String, String> map = unionService.unionOrderRevoke(orderId);
        return Mono.just(Result.success(map));
    }
    @PostMapping("/refund")
    @Operation(description = "云闪付订单退款接口")
    public Mono<Result<Map<String, String>>> unionOrderRefund(@RequestBody @Validated(ValidationGroups.UnionRefundValidation.class) UnionParamDTO paramDTO) {
        Map<String, String> map = unionService.unionOrderRefund(paramDTO);
        return Mono.just(Result.success(map));
    }
    @PostMapping("/notify/success")
    @Operation(description = "云闪付支付成功通知接口")
    public ResponseEntity<String> unionSuccessNotify(Map<String, String> paramsMap){
        boolean result = unionService.unionSuccessNotify(paramsMap);
        ResponseEntity<String> entity = null;
        if (result){
            entity = new ResponseEntity<>("OK", HttpStatus.OK);
        }else {
            String reqData = UnionUtil.mapToString(paramsMap, "UTF-8", true, false);
            entity = new ResponseEntity<>(reqData, HttpStatus.BAD_REQUEST);
        }
        return entity;
    }
    @PostMapping("/notify/refund")
    @Operation(description = "云闪付退款成功通知接口")
    public ResponseEntity<String> unionRefundNotify(Map<String, String> paramsMap){
        boolean result = unionService.unionRefundNotify(paramsMap);
        ResponseEntity<String> entity = null;
        if(result){
            entity = new ResponseEntity<>(HttpStatus.OK);
        }else {
            entity = new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
        }
        return entity;
    }
}
