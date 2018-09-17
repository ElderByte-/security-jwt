package com.elderbyte.security.spring.mock;

import com.elderbyte.security.ElderSecurityJwtSettings;
import com.elderbyte.security.mock.MockJwtHolder;
import com.elderbyte.security.rsa.RSAPublicKeyProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.server.WebFilter;

@Configuration
@ConditionalOnExpression("${elder.security.jwt.enableMock:${warden.client.enableMock:false}}==true")
public class ElderSecurityWebfluxMockConfiguration {

    @Bean
    public MockJwtHolder mockJwtHolder(ElderSecurityJwtSettings settings){
        return new MockJwtHolder(settings);
    }

    @Bean
    @Primary
    public RSAPublicKeyProvider mockRSAPublicKeyProvider(MockJwtHolder mockJwtHolder){
        return realm ->  mockJwtHolder.getMockPublicKey();
    }

    @Bean
    public WebFilter MockAuthenticationFilter(MockJwtHolder jwtHolder){
        return new MockAuthenticationFilter(jwtHolder);
    }

}
