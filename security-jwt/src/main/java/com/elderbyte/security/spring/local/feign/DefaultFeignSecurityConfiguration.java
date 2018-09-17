package com.elderbyte.security.spring.local.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The default configuration for a Feign Client
 */
@Configuration
@ConditionalOnClass(name = "feign.RequestInterceptor")
@Import({
        DefaultRequestInterceptorConfiguration.class,
})
public class DefaultFeignSecurityConfiguration {

}
