package com.elderbyte.security.spring.mock;

import com.elderbyte.security.mock.MockJwtHolder;
import com.elderbyte.security.spring.conditions.SecurityMockEnabledCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
@Conditional(SecurityMockEnabledCondition.class)
public class ElderSecurityWebfluxMockConfiguration {

    @Bean
    public WebFilter MockAuthenticationFilter(MockJwtHolder jwtHolder){
        return new MockAuthenticationFilter(jwtHolder);
    }

}
