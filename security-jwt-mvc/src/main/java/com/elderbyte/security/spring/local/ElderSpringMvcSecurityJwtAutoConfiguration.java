package com.elderbyte.security.spring.local;

import com.elderbyte.security.spring.local.config.MvcSecurityExceptionHandler;
import com.elderbyte.security.spring.local.config.DefaultElderSecurityConfig;
import com.elderbyte.security.spring.local.auth.RestAuthenticationEntryPoint;
import com.elderbyte.security.spring.local.feign.DefaultFeignSecurityConfiguration;
import com.elderbyte.security.spring.mock.ElderSpringSecurityMockConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@Import({
        ElderSpringSecurityMockConfiguration.class,

        MvcSecurityExceptionHandler.class,
        DefaultElderSecurityConfig.class,
        DefaultFeignSecurityConfiguration.class
})
public class ElderSpringMvcSecurityJwtAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }

}
