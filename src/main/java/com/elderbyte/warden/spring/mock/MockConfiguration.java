package com.elderbyte.warden.spring.mock;

import com.elderbyte.warden.spring.rsa.RSAPublicKeyProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty("warden.client.enableMock")
public class MockConfiguration {

    @Bean
    public MockJwtHolder mockJwtHolder(){
        return new MockJwtHolder();
    }

    @Bean
    @Primary
    public RSAPublicKeyProvider mockRSAPublicKeyProvider(MockJwtHolder mockJwtHolder){
        return realm ->  mockJwtHolder.getMockPublicKey();
    }

}
