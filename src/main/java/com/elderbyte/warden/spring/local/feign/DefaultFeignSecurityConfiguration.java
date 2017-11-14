package com.elderbyte.warden.spring.local.feign;

import com.elderbyte.warden.spring.local.feign.interceptors.AuthRequestInterceptor;
import com.elderbyte.warden.spring.local.feign.interceptors.RedirectCurrentUserRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The default configuration for a Feign Client
 */
@Configuration
public class DefaultFeignSecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthRequestInterceptor.class)
    public RequestInterceptor requestTokenBearerInterceptor() {
        return new RedirectCurrentUserRequestInterceptor();
    }

}
