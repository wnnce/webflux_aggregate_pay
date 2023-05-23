package com.zeroxn.pay.web;

import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;
import com.zeroxn.pay.core.mq.PayMQTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/5/23 下午12:34
 * @Description:
 */
@RestController
@RequestMapping("/mq")
public class MQController {
    private final PayMQTemplate mqTemplate;
    public MQController(PayMQTemplate mqTemplate){
        this.mqTemplate = mqTemplate;
    }
    @GetMapping("/su/we/{text}")
    public Mono<String> wechatSuccess(@PathVariable("text")String text){
        mqTemplate.send(PayPlatform.WECHAT, PayResult.SUCCESS, text);
        return Mono.just(text);
    }
    @GetMapping("/su/ali/{text}")
    public Mono<String> alipaySuccess(@PathVariable("text")String text){
        mqTemplate.send(PayPlatform.ALIPAY, PayResult.SUCCESS, text);
        String string = LocalDateTime.now().toString();
        return Mono.just(text + string);
    }
    @GetMapping("/re/we/{text}")
    public Mono<String> wechatRefund(@PathVariable("text")String text){
        mqTemplate.send(PayPlatform.WECHAT, PayResult.REFUND, text);
        return Mono.just(text);
    }
}
