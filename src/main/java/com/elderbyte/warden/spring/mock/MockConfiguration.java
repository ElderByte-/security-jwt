package com.elderbyte.warden.spring.mock;

import com.elderbyte.warden.spring.WardenSpringSecurityJwtSettings;
import com.elderbyte.warden.spring.rsa.RSAPublicKeyProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty("warden.client.enableMock")
public class MockConfiguration {

    @Bean
    public MockJwtService mockJwtService(WardenSpringSecurityJwtSettings settings){
        return new MockJwtService(settings);
    }

    @Bean
    @Primary
    public RSAPublicKeyProvider mockRSAPublicKeyProvider(MockJwtService mockJwtService){
        return realm ->  mockJwtService.getMockPublicKey();
    }
}
