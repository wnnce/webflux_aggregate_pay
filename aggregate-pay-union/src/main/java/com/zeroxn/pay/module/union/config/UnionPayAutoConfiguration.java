package com.zeroxn.pay.module.union.config;

import com.zeroxn.pay.core.PayTemplate;
import com.zeroxn.pay.core.config.PayAutoConfiguration;
import com.zeroxn.pay.module.union.UnionPayTemplate;
import com.zeroxn.pay.module.union.business.UnionPayBusiness;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 上午11:47
 * @Description: 云闪付自动配置类
 */
@AutoConfigureBefore(PayAutoConfiguration.class)
@EnableConfigurationProperties(UnionPayProperties.class)
public class UnionPayAutoConfiguration {
    @Bean
    public UnionModuleConfig unionModuleConfig(UnionPayProperties properties) {
        return new UnionModuleConfig(properties);
    }
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    @ConditionalOnClass(UnionPayProperties.class)
    public UnionPayBusiness unionPayBusiness(UnionPayProperties unionPayProperties, RestTemplate restTemplate){
        return new UnionPayBusiness(unionPayProperties, restTemplate);
    }
    @Bean
    @ConditionalOnClass(UnionPayBusiness.class)
    public PayTemplate unionPayTemplate(UnionPayBusiness unionPayBusiness, UnionPayProperties unionPayProperties){
        return new UnionPayTemplate(unionPayBusiness, unionPayProperties);
    }
}
