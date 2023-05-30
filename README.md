# 一个基于`Spring-Webflux`的聚合支付模块

从一个聚合支付的工具代码迁移而来，准备做成独立的模块。通过请求对应的接口来调用对应的支付平台的功能，支付成功或退款成功的通知通过消息队列实现。

**暂停更新 预计添加京东支付和云闪付**

## 如何使用

1. 补全`com.zeroxn.pay.module`包内所有子包`XXXXConstant`常量类中的参数

2. 新建`ApiAdapter`对象，使用`PayParam`类中的构造器构造请求参数对象，**只要是构造器需要的参数都是必须的**

3. 调用`ApiAdapter`对象中的对应方法，某些方法可能需要传入泛型。

   ```java
   // 下单方法的泛型
   // AlipayTradeCreateResponse.class 支付宝小程序支付
   // AlipayTradeWapPayResponse.class 支付宝WAP手机网站支付
   // AlipayTradePagePayResponse.class 支付宝电脑端网站下单
   // PrepayResponse.class 微信H5下单
   // PrepayWithRequestPaymentResponse.class 微信小程序下单
   
   // 查询订单方法的泛型
   // AlipayTradeQueryResponse.class 查询支付宝订单
   // Transaction.class 查询微信订单
   
   // 退款订单的泛型
   // Refund.class 微信支付退款
   // AlipayTradeRefundResponse.class 支付宝退款
   
   // 查询退款订单的泛型
   // Refund.class 查询微信支付退款订单
   // AlipayTradeFastpayRefundQueryResponse.class 查询支付宝支付退款订单
   ```

4. `XXXXHandler`类中的错误处理可能需要按照业务逻辑更改

## 异步通知解密

### 微信异步通知

需要在`Controller`层新建两个接口用来接收微信支付成功和退款成功的异步回调通知，通知解密方法封装在了`WechatPayUtils`类中

[官方文档](https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_5.shtml)

```java
// 一个请求示例

/**
  * 接收支付成功后微信服务器发送的回调通知
  * @param requestBody 请求体
  * @param signTure 请求头
  * @param nonce 请求头 随机字符串
  * @param timestamp 请求头 时间戳
  * @param serial 请求头 解密需要的参数
  * @param signType 请求头 加密方式
  */
//@PostMapping("/notify/success")
public void paySuccessNotify(@RequestBody String requestBody, 
                             @RequestHeader("Wechatpay-Signature") String signTure,
                             @RequestHeader("Wechatpay-Nonce") String nonce,
                             @RequestHeader("Wechatpay-Timestamp") String timestamp,
                             @RequestHeader("Wechatpay-Serial") String serial,
                             @RequestHeader("Wechatpay-Signature-Type") String signType){
    // 得到通知对象
    Transaction transaction = WeChatPayUtils.successNotifyDecrypt(signTure, signType, 
                                                                  nonce, timestamp, serial, requestBody);
    payService.orderPaySuccess(transaction);
}
```

### 支付宝异步通知

[官方文档](https://opendocs.alipay.com/open/270/105902?pathHash=d5cd617e&ref=api)，只封装了通知内容验签的方法，封装在`AlipayUtils`类中

## 最后

测试支付方法所需的参数没法获取，所以无法测试。