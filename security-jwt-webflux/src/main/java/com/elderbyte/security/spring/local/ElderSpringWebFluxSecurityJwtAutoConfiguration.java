package com.elderbyte.security.spring.local;

import com.elderbyte.security.spring.conditions.ReactiveWebApplicationCondition;
import com.elderbyte.security.spring.local.config.DefaultElderWebfluxSecurityConfig;
import com.elderbyte.security.spring.mock.ElderSecurityWebfluxMockConfiguration;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Conditional(ReactiveWebApplicationCondition.class)
@Import({
        ElderSecurityWebfluxMockConfiguration.class,
        DefaultElderWebfluxSecurityConfig.class,
})
public class ElderSpringWebFluxSecurityJwtAutoConfiguration {

}
