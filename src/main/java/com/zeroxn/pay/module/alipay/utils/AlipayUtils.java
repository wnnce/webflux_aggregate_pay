package com.zeroxn.pay.module.alipay.utils;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 09:48
 * @Description: 支付宝工具类
 */
@Slf4j
public class AlipayUtils {

    /**
     * 支付宝异步通知验签方法 异步通过处理参考：https://opendocs.alipay.com/open/270/105902?pathHash=d5cd617e&ref=api
     * @param paramsMap 通知的所有参数
     * @return true：成功 false：失败
     * @throws AlipayApiException 支付宝API调用异常
     */
//    public static boolean signVerified(Map<String, String> paramsMap) throws AlipayApiException {
//        return AlipaySignature.rsaCheckV1(paramsMap, AlipayConstant.getAlipayPublicKey(), AlipayConstant.getCharset(), AlipayConstant.getSignType());
//    }

    /**
     * 工具方法 生成支付宝请求参数的请求体 传入请求参数 返回json格式的字符串
     * @param args 请求参数 参数格式为 kye, value, key, value
     * @return json格式的字符串或空
     */
    public static String makeBizContent(Object ...args){
        if(args.length % 2 != 0){
            log.warn("参数错误，参数必须为key,value形式！");
            return null;
        }
        JSONObject json = new JSONObject();
        for(int i = 0; i < args.length; i += 2){
            String key = String.valueOf(args[i]);
            Object value = args[i + 1];
            json.put(key, value);
        }
        return json.toJSONString();
    }

}
