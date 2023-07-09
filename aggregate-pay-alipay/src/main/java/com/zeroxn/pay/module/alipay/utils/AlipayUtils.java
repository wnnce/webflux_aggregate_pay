package com.zeroxn.pay.module.alipay.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/6/23 下午3:42
 * @Description:
 */
public class AlipayUtils {
    private static final Logger logger = LoggerFactory.getLogger(AlipayUtils.class);

    /**
     * 支付宝异步通知验签方法 异步通过处理参考：https://opendocs.alipay.com/open/270/105902?pathHash=d5cd617e&ref=api
     * @param paramsMap 通知的所有参数
     * @return true：成功 false：失败
     */
    public static boolean signVerified(Map<String, String> paramsMap, String publicKey, String charSet, String signType) {
        try{
            return AlipaySignature.rsaCheckV1(paramsMap, publicKey, charSet, signType);
        }catch (AlipayApiException ex){
            logger.error("支付宝通知验签失败，错误消息：{}", ex.getMessage());
            return false;
        }
    }
}
