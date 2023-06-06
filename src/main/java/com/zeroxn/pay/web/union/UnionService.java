package com.zeroxn.pay.web.union;

import com.zeroxn.pay.core.entity.PayParams;
import com.zeroxn.pay.core.enums.PayMethod;
import com.zeroxn.pay.module.union.UnionPayTemplate;
import com.zeroxn.pay.module.union.utils.UnionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/6/6 下午7:27
 * @Description: 云闪付支付Service层
 */
@Service
@ConditionalOnBean(UnionPayTemplate.class)
public class UnionService {
    private static final Logger logger = LoggerFactory.getLogger(UnionService.class);
    private final UnionPayTemplate payTemplate;
    public UnionService(UnionPayTemplate payTemplate){
        this.payTemplate = payTemplate;
    }

    /**
     * 云闪付WAP下单，返回URL编码后的支付表单，引号在被序列化时会被加上反斜杠 使用URL编码解决
     * @param paramDTO 支付参数
     * @return 被URL编码后的支付表单
     */
    public String unionWapPay(UnionParamDTO paramDTO){
        PayParams params = new PayParams.BuilderUnionWap()
                .setOrderId(paramDTO.getOrderId())
                .setTotal(paramDTO.getTotal())
                .setFrontUrl(paramDTO.getFrontUrl())
                .setDescription(paramDTO.getDescription())
                .build();
        String result = payTemplate.confirmOrder(params, PayMethod.WAP, String.class);
        UnionService.logger.info("云闪付生成支付参数成功，订单号：{}", params.getOrderId());
        result = URLEncoder.encode(result, StandardCharsets.UTF_8);
        return result;
    }

    /**
     * 通过商户系统内的订单号查询云闪付支付信息
     * @param orderId 商户系统内的订单ID
     * @return 云闪付系统内的支付信息 key-value形式
     */
    public Map<String, String> queryUnionOrder(String orderId){
        String result = payTemplate.queryOrder(orderId, null, String.class);
        Map<String, String> map = UnionUtil.stringToMap(result, "&", "=");
        map.remove("signPubKeyCert");
        return map;
    }
}