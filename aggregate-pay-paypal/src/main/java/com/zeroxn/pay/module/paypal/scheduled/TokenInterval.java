package com.zeroxn.pay.module.paypal.scheduled;

import com.zeroxn.pay.module.paypal.business.PaypalBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 下午5:14
 * @Description: Token定时任务类
 */
public class TokenInterval {
    private static final Logger logger = LoggerFactory.getLogger(TokenInterval.class);
    private final PaypalBusiness paypalBusiness;
    public TokenInterval(PaypalBusiness paypalBusiness) {
        this.paypalBusiness = paypalBusiness;
    }
    @Scheduled(cron = "0 */4 * * *")
    public void execute() {
        logger.info("开始定时获取PaypalToken....");
        CompletableFuture.runAsync(paypalBusiness::initAuthorizationToken);
    }
}
