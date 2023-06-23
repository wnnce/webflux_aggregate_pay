package com.zeroxn.pay.test;

import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.module.union.UnionPayTemplate;
import com.zeroxn.pay.module.union.utils.UnionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/5/18 下午5:30
 * @Description:
 */
@SpringBootTest
public class TestUtil {
    @Autowired
    private UnionPayTemplate unionPayTemplate;

    @Test
    public void testMapFormatString() throws UnsupportedEncodingException {
        PayParams params = new PayParams.BuilderUnionWap()
                .setOrderId("236623432453453")
                .setTotal(100)
                .setDescription("测试")
                .setFrontUrl("http://xx")
                .build();
        String result = unionPayTemplate.confirmOrder(params, PayMethod.WAP, String.class);
        System.out.println(result);
    }
    @Test
    public void testUnionQueryOrder(){
        String result = unionPayTemplate.queryOrder("236623432453453", null, String.class);
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
        String result = unionPayTemplate.refundOrder(params, String.class);
        System.out.println(result);
    }
}
