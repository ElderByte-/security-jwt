package com.elderbyte.security.spring.local.feign;

import com.elderbyte.security.spring.local.feign.interceptors.AuthRequestInterceptor;
import com.elderbyte.security.spring.local.feign.interceptors.RedirectCurrentUserRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class DefaultRequestInterceptorConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthRequestInterceptor.class)
    public RequestInterceptor requestTokenBearerInterceptor() {
        return new RedirectCurrentUserRequestInterceptor();
    }
}
