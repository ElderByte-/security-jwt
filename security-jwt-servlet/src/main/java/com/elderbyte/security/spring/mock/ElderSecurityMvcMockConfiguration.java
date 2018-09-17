package com.elderbyte.security.spring.mock;

import com.elderbyte.security.mock.MockJwtHolder;
import com.elderbyte.security.spring.settings.ElderSecurityJwtSettingsFallback;
import com.elderbyte.security.rsa.RSAPublicKeyProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.servlet.Filter;

@Configuration
@ConditionalOnExpression("${elder.security.jwt.enableMock:${warden.client.enableMock:false}}==true")
public class ElderSecurityMvcMockConfiguration {

    @Bean
    public MockJwtHolder mockJwtHolder(ElderSecurityJwtSettingsFallback settings){
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
