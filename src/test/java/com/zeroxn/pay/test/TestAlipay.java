package com.zeroxn.pay.test;

import com.alipay.api.AlipayClient;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.zeroxn.pay.core.entity.PayParam;
import com.zeroxn.pay.module.alipay.config.AlipayPayConfig;
import com.zeroxn.pay.module.alipay.service.AlipayPayService;
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
    private AlipayPayService alipayService;
    @Autowired
    private AlipayClient alipayClient;
    @Test
    public void testAlipay(){
        PayParam param = new PayParam.BuilderAliPayWap()
                .setOrderId("70501111111S001111119")
                .setTotal(900)
                .setDescription("乐透")
                .setQuitUrl(" ")
                .setReturnUrl(" ")
                .build();
        AlipayTradeWapPayResponse response = alipayService.wapConfirmOrder(param);
        if (response != null && response.isSuccess()){
            System.out.println(response.getBody());
        }else {
            System.out.println("error");
        }
    }
    @Test
    public void testAlipayDesktop(){
        PayParam param = new PayParam.BuilderAlipayDesktop()
                .setOrderId("20150320010101001")
                .setTotal(8888)
                .setDescription("Iphone6 16G")
                .build();
        AlipayTradePagePayResponse response = alipayService.desktopConfirmOrder(param);
        if(response != null && response.isSuccess()){
            System.out.println(response.getBody());
        }else {
            System.out.println("error");
        }
    }
    @Test
    public void testAlipayQuery(){
        AlipayTradeQueryResponse response = alipayService.queryOrderByOrderId("20150320010101001");
        System.out.println(response.getBody());
    }
    @Test
    public void testAlipayClose(){
        AlipayTradeCloseResponse response = alipayService.closeOrder("20150320010101001");
        if (response != null && response.isSuccess()){
            System.out.println(response.getBody());
        }else {
            System.out.println("error");
        }
    }
}
