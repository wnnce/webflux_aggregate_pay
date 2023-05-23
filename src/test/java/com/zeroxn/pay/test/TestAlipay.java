package com.zeroxn.pay.test;

import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;
import com.zeroxn.pay.core.mq.PayMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: lisang
 * @DateTime: 2023/5/18 下午5:30
 * @Description:
 */
@SpringBootTest
public class TestAlipay {
    @Autowired
    private PayMQTemplate mqTemplate;
    @Test
    public void testAlipayDesktop(){
        mqTemplate.send(PayPlatform.ALIPAY, PayResult.SUCCESS, "Success");
    }
}
