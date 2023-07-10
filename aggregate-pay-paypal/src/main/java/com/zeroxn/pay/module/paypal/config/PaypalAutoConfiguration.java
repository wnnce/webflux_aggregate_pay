package com.zeroxn.pay.module.paypal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.zeroxn.pay.core.config.PayAutoConfiguration;
import com.zeroxn.pay.module.paypal.async.PaypalAsyncTask;
import com.zeroxn.pay.module.paypal.business.PaypalBusiness;
import com.zeroxn.pay.module.paypal.scheduled.TokenInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: lisang
 * @DateTime: 2023/7/9 下午12:39
 * @Description: Paypal自动配置类
 */
@AutoConfigureBefore(PayAutoConfiguration.class)
@EnableConfigurationProperties(PaypalProperties.class)
public class PaypalAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public PaypalBusiness paypalBusiness(RestTemplate restTemplate, PaypalProperties properties) {
        return new PaypalBusiness(properties, restTemplate);
    }
    @Bean
    public PaypalAsyncTask paypalAsyncTask(PaypalBusiness business) {
        PaypalAsyncTask paypalAsyncTask = new PaypalAsyncTask(business);
        paypalAsyncTask.initAuthorizationToken();
        return paypalAsyncTask;
    }
    @Bean
    public TokenInterval tokenInterval(PaypalAsyncTask paypalAsyncTask) {
        return new TokenInterval(paypalAsyncTask);
    }
}
