package com.elderbyte.security.spring.mock;

import com.elderbyte.security.ElderSecurityJwtSettings;
import com.elderbyte.security.mock.MockJwtHolder;
import com.elderbyte.security.spring.conditions.SecurityMockEnabledCondition;
import com.elderbyte.security.rsa.RSAPublicKeyProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
@Conditional(SecurityMockEnabledCondition.class)
public class ElderSecurityMockConfiguration {

    @Bean
    public MockJwtHolder mockJwtHolder(ElderSecurityJwtSettings settings){
        return new MockJwtHolder(settings);
    }

    @Bean
    @Primary
    public RSAPublicKeyProvider mockRSAPublicKeyProvider(MockJwtHolder mockJwtHolder){
        return realm ->  mockJwtHolder.getMockPublicKey();
    }

}