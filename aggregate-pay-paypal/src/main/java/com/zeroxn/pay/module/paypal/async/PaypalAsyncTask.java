package com.zeroxn.pay.module.paypal.async;

import com.zeroxn.pay.module.paypal.business.PaypalBusiness;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 下午4:59
 * @Description: Paypal支付模块的异步线程类
 */
@Async
public class PaypalAsyncTask {
    private final PaypalBusiness paypalBusiness;

    public PaypalAsyncTask(PaypalBusiness paypalBusiness) {
        this.paypalBusiness = paypalBusiness;
    }
    public void initAuthorizationToken() {
        paypalBusiness.getAuthorizationToken();
    }
}
