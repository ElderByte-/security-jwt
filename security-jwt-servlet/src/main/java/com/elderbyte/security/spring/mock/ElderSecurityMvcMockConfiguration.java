package com.elderbyte.security.spring.mock;

import com.elderbyte.security.mock.MockJwtHolder;
import com.elderbyte.security.spring.conditions.SecurityMockEnabledCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@Conditional(SecurityMockEnabledCondition.class)
public class ElderSecurityMvcMockConfiguration {

    @Bean
    public Filter MockAuthenticationFilter(MockJwtHolder jwtHolder){
        return new MockAuthenticationServletFilter(jwtHolder);
    }

}
