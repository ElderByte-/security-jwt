package com.elderbyte.security.spring.local;

import com.elderbyte.security.spring.settings.ElderSpringSecurityJwtSettingsConfig;
import com.elderbyte.security.spring.local.auth.LocalAuthService;
import com.elderbyte.security.spring.local.config.AccessDeniedExceptionHandler;
import com.elderbyte.security.spring.local.config.DefaultElderSecurityConfig;
import com.elderbyte.security.spring.local.auth.RestAuthenticationEntryPoint;
import com.elderbyte.security.spring.local.feign.DefaultFeignSecurityConfiguration;
import com.elderbyte.security.spring.mock.ElderSpringSecurityMockConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableConfigurationProperties
@Import({
        ElderSpringSecurityMockConfiguration.class,

        ElderSpringSecurityJwtSettingsConfig.class,
        AccessDeniedExceptionHandler.class,
        DefaultElderSecurityConfig.class,
        DefaultFeignSecurityConfiguration.class
})
public class ElderSpringMvcSecurityJwtAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public LocalAuthService LocalAuthService() {
        return new LocalAuthService();
    }
}
