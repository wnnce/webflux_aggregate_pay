package com.zeroxn.pay.module.wechat.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.zeroxn.pay.core.PayTemplate;
import com.zeroxn.pay.core.config.PayAutoConfiguration;
import com.zeroxn.pay.module.wechat.WechatPayTemplate;
import com.zeroxn.pay.module.wechat.business.h5.WechatPayH5Business;
import com.zeroxn.pay.module.wechat.business.jsapi.WechatPayJsapiBusiness;
import com.zeroxn.pay.module.wechat.business.parser.WechatNotifyParser;
import com.zeroxn.pay.module.wechat.business.refund.WechatRefundBusiness;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Author: lisang
 * @DateTime: 2023/5/20 下午7:23
 * @Description: 微信支付自动配置类
 */
@AutoConfigureBefore(PayAutoConfiguration.class)
@EnableConfigurationProperties(WechatPayProperties.class)
public class WechatPayAutoConfiguration {
    @Bean
    public WechatModuleConfig wechatModuleConfig() {
        return new WechatModuleConfig();
    }
    @Bean
    @ConditionalOnClass(WechatPayProperties.class)
    public Config config(WechatPayProperties payConfig){
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(payConfig.getMerchantId())
                .apiV3Key(payConfig.getApiV3Key())
                .merchantSerialNumber(payConfig.getMerchantSerialNumber())
                .privateKey(payConfig.getPrivateKey())
                .build();
    }
    @Bean
    public WechatPayH5Business wechatPayH5Service(Config config, WechatPayProperties wechatPayConfig){
        return new WechatPayH5Business(config, wechatPayConfig);
    }
    @Bean
    public WechatPayJsapiBusiness wechatPayJsapiService(Config config, WechatPayProperties wechatPayConfig){
        return new WechatPayJsapiBusiness(config, wechatPayConfig);
    }
    @Bean
    public WechatRefundBusiness wechatRefundService(Config config, WechatPayProperties wechatPayConfig){
        return new WechatRefundBusiness(config, wechatPayConfig);
    }
    @Bean
    public WechatNotifyParser wechatNotifyParser(Config config){
        return new WechatNotifyParser(config);
    }
    @Bean
    @ConditionalOnClass(value = {WechatPayH5Business.class, WechatPayJsapiBusiness.class, WechatRefundBusiness.class})
    public PayTemplate wechatPayTemplate(WechatPayH5Business h5Service, WechatPayJsapiBusiness jsapiService, WechatRefundBusiness refundService){
        return new WechatPayTemplate(h5Service, jsapiService, refundService);
    }

}
