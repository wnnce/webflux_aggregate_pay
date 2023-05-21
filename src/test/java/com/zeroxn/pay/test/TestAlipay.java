package com.zeroxn.pay.test;

import com.alipay.api.AlipayClient;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;
import com.zeroxn.pay.core.interfaces.PayMQTemplate;
import com.zeroxn.pay.core.mq.constant.RabbitConstant;
import com.zeroxn.pay.module.alipay.config.AlipayPayProperties;
import com.zeroxn.pay.module.alipay.business.AlipayPayBusiness;
import com.zeroxn.pay.web.alipay.AlipayParamDTO;
import com.zeroxn.pay.web.alipay.AlipayService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
