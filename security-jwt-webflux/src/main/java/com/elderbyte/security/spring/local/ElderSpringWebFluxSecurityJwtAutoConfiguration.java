package com.elderbyte.security.spring.local;

import com.elderbyte.security.spring.local.config.DefaultElderWebfluxSecurityConfig;
import com.elderbyte.security.spring.mock.ElderSecurityWebfluxMockConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ElderSecurityWebfluxMockConfiguration.class,

        DefaultElderWebfluxSecurityConfig.class,
})
public class ElderSpringWebFluxSecurityJwtAutoConfiguration {

}
