package com.zeroxn.pay.test;

import com.alipay.api.AlipayClient;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.ResultCode;
import com.zeroxn.pay.core.exception.AlipayPayException;
import com.zeroxn.pay.module.alipay.config.AlipayPayConfig;
import com.zeroxn.pay.module.alipay.service.AlipayPayService;
import com.zeroxn.pay.web.alipay.AlipayParamDTO;
import com.zeroxn.pay.web.alipay.AlipayService;
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
    private AlipayPayConfig alipayPayConfig;
    @Autowired
    private AlipayPayService alipayPayService;
    @Autowired
    private AlipayService alipayService;
    @Autowired
    private AlipayClient alipayClient;
    @Test
    public void testAlipay(){
        AlipayParamDTO paramDTO = new AlipayParamDTO();
        paramDTO.setOrderId("70501111111S001111119");
        paramDTO.setTotal(900);
        paramDTO.setDescription("大乐透");
        paramDTO.setQuitUrl("");
        String str = alipayService.alipayWapPay(paramDTO);
        System.out.println(str);
    }
    @Test
    public void testAlipayDesktop(){

    }
    @Test
    public void testAlipayQuery(){
        AlipayTradeQueryResponse response = alipayPayService.queryOrderByOrderId("70501111111S001111119");

    }
    @Test
    public void testAlipayClose(){
        AlipayTradeCloseResponse response = alipayPayService.closeOrder("20150320010101001");
        if (response != null && response.isSuccess()){
            System.out.println(response.getBody());
        }else {
            System.out.println("error");
        }
    }
}
