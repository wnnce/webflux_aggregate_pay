package com.zeroxn.pay.web.wechat;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Author: lisang
 * @DateTime: 2023/5/20 下午6:25
 * @Description:
 */
@RestController
@RequestMapping("/pay/wechat")
@Tag(name = "微信支付接口")
public class WechatController {
    @GetMapping
    public Mono<String> wechatTest(){
        return Mono.just("success");
    }
}
