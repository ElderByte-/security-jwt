package com.elderbyte.security.spring.local;

import com.elderbyte.security.spring.settings.ElderSpringSecurityJwtSettingsConfig;
import com.elderbyte.security.spring.local.auth.LocalAuthService;
import com.elderbyte.security.spring.local.config.DefaultElderSecurityConfig;
import com.elderbyte.security.spring.local.feign.DefaultFeignSecurityConfiguration;
import com.elderbyte.security.spring.mock.ElderSpringSecurityMockConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties
@Import({
        ElderSpringSecurityMockConfiguration.class,

        ElderSpringSecurityJwtSettingsConfig.class,
        DefaultElderSecurityConfig.class,
        DefaultFeignSecurityConfiguration.class
})
public class ElderSpringWebFluxSecurityJwtAutoConfiguration {

    @Bean
    public LocalAuthService LocalAuthService() {
        return new LocalAuthService();
    }
}
