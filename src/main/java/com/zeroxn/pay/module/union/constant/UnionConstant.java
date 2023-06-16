package com.zeroxn.pay.module.union.constant;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 下午8:33
 * @Description: 云闪付常量类
 */
public class UnionConstant {
    public static final String VERSION = "5.1.0";
    public static final String BIZ_TYPE = "000201";
    public static final String ACCESS_TYPE = "0";
    public static final String DESKTOP_CHANNEL_TYPE = "07";
    public static final String WAP_CHANNEL_TYPE = "08";
    public static final String SIGN_CERT_TYPE = "PKCS12";
    public static final String CERT_TYPE = "X.509";
    public static final String SIGN_METHOD_RSA = "01";
    public static final String SIGN_METHOD_SHA256 = "11";
    public static final String SIGN_METHOD_SM3 = "12";
    public static final String FRONT_URL = "https://gateway.95516.com/gateway/api/frontTransReq.do";
    public static final String TEST_FRONT_URL = "https://gateway.test.95516.com/gateway/api/frontTransReq.do";
    public static final String QUERY_URL = "https://gateway.95516.com/gateway/api/queryTrans.do";
    public static final String TEST_QUERY_URL = "https://gateway.test.95516.com/gateway/api/queryTrans.do";
    public static final String REFUND_URL = "https://gateway.95516.com/gateway/api/backTransReq.do";
    public static final String TEST_REFUND_URL = "https://gateway.test.95516.com/gateway/api/backTransReq.do";
}
