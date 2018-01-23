package com.elderbyte.warden.spring.mock;

import com.elderbyte.warden.spring.WardenSpringSecurityJwtSettings;
import com.elderbyte.warden.spring.rsa.RSAPublicKeyProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.servlet.Filter;

@Configuration
@ConditionalOnProperty("warden.client.enableMock")
public class WardenSpringSecurityMockConfiguration {

    @Bean
    public MockJwtHolder mockJwtHolder(WardenSpringSecurityJwtSettings settings){
        return new MockJwtHolder(settings);
    }

    @Bean
    @Primary
    public RSAPublicKeyProvider mockRSAPublicKeyProvider(MockJwtHolder mockJwtHolder){
        return realm ->  mockJwtHolder.getMockPublicKey();
    }

    @Bean
    public Filter MockAuthenticationFilter(MockJwtHolder jwtHolder){
        return new MockAuthenticationFilter(jwtHolder);
    }

}
