package com.zeroxn.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午2:54
 * @Description: 项目启动主类
 */

@SpringBootApplication
@EnableScheduling
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class);
    }
}
