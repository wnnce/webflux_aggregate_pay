package com.zeroxn.pay.module.union.constant;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 下午8:33
 * @Description: 云闪付常量类
 */
public class UnionConstant {
    public static final String VERSION = "5.1.0";
    public static final String BIZTYPE = "000201";
    public static final String ACCESSTYPE = "0";
    public static final String DESKTOPCHANNELTYPE = "07";
    public static final String WAPCHANNELTYPE = "08";
    public static final String SIGNCERTTYPE = "PKCS12";
    public static final String CERTTYPE = "X.509";
    public static final String FRONTURL = "https://gateway.95516.com/gateway/api/frontTransReq.do";
    public static final String TESTFRONTURL = "https://gateway.test.95516.com/gateway/api/frontTransReq.do";
    public static final String QUERYURL = "https://gateway.95516.com/gateway/api/queryTrans.do";
    public static final String TESTQUERYURL = "https://gateway.test.95516.com/gateway/api/queryTrans.do";
    public static final String REFUNDURL = "https://gateway.95516.com/gateway/api/backTransReq.do";
    public static final String TESTREFUNDURL = "https://gateway.test.95516.com/gateway/api/backTransReq.do";
}
