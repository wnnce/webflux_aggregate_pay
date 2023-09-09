package com.zeroxn.pay;

import com.zeroxn.pay.module.jdpay.config.EnableJdPay;
import com.zeroxn.pay.module.paypal.config.EnablePaypalPay;
import com.zeroxn.pay.module.union.config.EnableUnionPay;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午2:54
 * @Description: 项目启动主类
 */

@SpringBootApplication
@EnableJdPay
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class);
    }
}
