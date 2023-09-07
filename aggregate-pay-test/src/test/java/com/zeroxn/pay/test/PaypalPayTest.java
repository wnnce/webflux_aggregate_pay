package com.zeroxn.pay.test;

import com.zeroxn.pay.module.paypal.business.PaypalBusiness;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 下午4:10
 * @Description:
 */
@SpringBootTest
public class PaypalPayTest {

    @Autowired
    private PaypalBusiness paypalBusiness;

    @Test
    public void testGetAuthToken() {

    }

    @Test
    public void testCreaterder() {
        String result = paypalBusiness.createOrder("45315513113212", "测试订单", 10000, "https://www.baidu.com", "https://www.baidu.com");
        System.out.println(result);
    }

    @Test
    public void testQueryOrder() {
        String result = paypalBusiness.queryOrder("1KH983898U558151G");
        System.out.println(result);
    }

    @Test
    public void testConfirmOrder() {
        String result = paypalBusiness.confirmOrder("1KH983898U558151G");
        System.out.println(result);
    }

    @Test
    public void testRefundOrder() {
        String result = paypalBusiness.refundOrder("8W915719D6330002U");
        System.out.println(result);
    }

    @Test
    public void testQueryRefund() {
        String result = paypalBusiness.queryRefundOrder("06266284FF6920043");
        System.out.println(result);
    }
}
