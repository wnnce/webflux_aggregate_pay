# 一个基于`Spring-Webflux`的聚合支付模块

基于`Spring-Flux`的聚合支付模块，可以作为独立的支付模块使用，实现了支付平台使用的模块化和插拔式。目前支持微信支付、支付宝、云闪付，后面预计将支持（"Paypal、京东支付"）

## 快速开始

项目分为启动模块、核心模块、支付平台模块三部分

- 启动模块：项目整体的启动、编写配置文件
- 核心模块：项目核心配置类、消息队列、项目异常和异常处理、枚举和参数验证
- 支付平台模块：各支付平台的具体实现

消息队列有`RabbitMQ`和`Kafka`两种实现，需要搭建好消息队列并在配置文件中启用，如果同时启用两个那么会以`RabbitMQ`优先

项目启动后可以通过[http://localhost:8081/pay/swagger-ui.html](http://localhost:8081/pay/swagger-ui.html)查看项目的接口文档，接口文档使用`Sping-doc`实现

1. `Clone`该项目

```bash
git clone https://github.com/wnnce/webflux_aggregate_pay.git
```

2. 使用`Idea`打开项目，拉取`maven`依赖

3. 配置`RabbitMQ`或`Kafka`环境

4. 在项目启动类添加`Enablexxx`注解，开启某个支付平台的自动配置

   ```java
   @SpringBootApplication
   // 开启云闪付支付
   @EnableUnionPay
   public class PayApplication {
       public static void main(String[] args) {
           SpringApplication.run(PayApplication.class);
       }
   }
   ```

5. 修改配置文件

   ```yaml
   pay:
     # 云闪付的配置项
     union:
       merchant-id: 777290058203758
       sign-cert-path: classpath:certs/acp_test_sign.pfx
       sign-cert-pwd: '000000'
       sign-type: '01'
       success-notify-url: http://xxx.123.com
       refund-notify-url: http://xxx.213.com
     mq:
     	# 使用RabbitMQ作为消息队列的实现
       rabbitmq:
         enable: true
   ```

6. 启动项目并查看接口文档

## 添加支付平台

如果需要添加其它的支付平台，核心模块中定义了支付的顶级接口`PayTemplate`和支付模块配置接口`PayModuleConfigurer`，实现`PayTemplate`接口用于实现该平台的具体支付逻辑，然后再实现`PayModuleConfigurer`接口中的方法，用于向核心包注册当前模块。最后在完成`Web`接口和接口文档注释即可

```java
// 顶级支付模板接口
public interface PayTemplate {
    <T> T confirmOrder(PayParams param, PayMethod method, Class<T> clazz);
    <T> T closeOrder(String orderId, PayMethod method, Class<T> clazz);
    <T> T queryOrder(String orderId, PayMethod method, Class<T> clazz);
    <T> T refundOrder(PayParams param, Class<T> clazz);
    <T> T queryRefundOrder(String orderId, String orderRefundId, Class<T> clazz);
}
```

```java
// 实现PayModuleConfigurer接口 向核心模块注册当前接口 后续会添加更多配置
public class WechatModuleConfig implements PayModuleConfigurer {
    @Override
    public void addModule(ModuleRegistry registry) {
        registry.addModule("wechat");
    }
}
```

```java
// 开启模块注解的实现 当Spring扫描到这个注解后 就会通过引入的自动配置类来实现支付平台的自动配置
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnClass(Config.class)
// 引入微信支付自动配置类
@Import(WechatPayAutoConfiguration.class)
public @interface EnableWechatPay {
}
```

## 最后

项目是从一个多平台支付的工具代码迁移过来的，最初的愿景是实现一个基于`Spring-flux`的聚合支付模块，暂时先实现基础的支付功能。（随缘开发，暑假做项目）