package com.zeroxn.pay.test;

import com.zeroxn.pay.module.paypal.business.PaypalBusiness;
import com.zeroxn.pay.module.paypal.config.EnablePaypalPay;
import com.zeroxn.pay.module.paypal.config.PaypalAutoConfiguration;
import com.zeroxn.pay.module.paypal.constant.PaypalConstant;
import com.zeroxn.pay.module.paypal.dto.AuthResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
}
