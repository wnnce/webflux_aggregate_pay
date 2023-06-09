package com.zeroxn.pay.test;

import com.zeroxn.pay.core.PayTemplate;
import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.module.union.utils.UnionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午3:48
 * @Description:
 */
@SpringBootTest
public class UnionPayTest {
    @Autowired
    @Qualifier("unionPayTemplate")
    private PayTemplate payTemplate;

    @Test
    public void testMapFormatString() throws UnsupportedEncodingException {
        PayParams params = new PayParams.BuilderUnionWap()
                .setOrderId("2366234324534123")
                .setTotal(100)
                .setDescription("测试")
                .setFrontUrl("http://xx")
                .build();
        String result = payTemplate.confirmOrder(params, PayMethod.WAP, String.class);
        System.out.println(result);
    }
    @Test
    public void testUnionQueryOrder(){
        String result = payTemplate.queryOrder("236623432453453", null, String.class);
        Map<String, String> map = UnionUtils.stringToMap(result, "&", "=");
        System.out.println(map);
    }
    @Test
    public void testUnionRefundOrder(){
        PayParams params = new PayParams.BuilderRefund()
                .setOrderId("236623432453453")
                .setOrderRefundId("632306061513128662778")
                .setTotal(100)
                .setRefundTotal(50)
                .setRefundDescription("退款")
                .build();
        String result = payTemplate.refundOrder(params, String.class);
        System.out.println(result);
    }
}
