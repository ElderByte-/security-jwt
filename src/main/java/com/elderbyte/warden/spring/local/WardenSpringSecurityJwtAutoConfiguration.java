package com.elderbyte.warden.spring.local;

import com.elderbyte.warden.spring.WardenSpringSecurityJwtSettings;
import com.elderbyte.warden.spring.local.auth.LocalAuthService;
import com.elderbyte.warden.spring.local.config.AccessDeniedExceptionHandler;
import com.elderbyte.warden.spring.local.config.DefaultWardenSecurityConfig;
import com.elderbyte.warden.spring.local.auth.RestAuthenticationEntryPoint;
import com.elderbyte.warden.spring.local.jwt.DefaultJwtAuthenticationProvider;
import com.elderbyte.warden.spring.local.jwt.DefaultJwtTokenConverter;
import com.elderbyte.warden.spring.local.jwt.JWSVerifierService;
import com.elderbyte.warden.spring.local.jwt.JwtTokenConverter;
import com.elderbyte.warden.spring.rsa.RSAPublicKeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@Import({
        AccessDeniedExceptionHandler.class,
        DefaultWardenSecurityConfig.class,
})
public class WardenSpringSecurityJwtAutoConfiguration {

    @Autowired
    private WardenSpringSecurityJwtSettings clientSettings;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(JWSVerifierService jwsVerifierService){
        return new DefaultJwtAuthenticationProvider(jwsVerifierService);
    }

    @Bean
    @ConditionalOnMissingBean(JwtTokenConverter.class)
    public JwtTokenConverter tokenConverter(){
        return new DefaultJwtTokenConverter();
    }
    
    @Bean
    public LocalAuthService LocalAuthService() {
        return new LocalAuthService();
    }

    @Bean
    @ConditionalOnMissingBean(JWSVerifierService.class)
    public JWSVerifierService jwsVerifierService(RSAPublicKeyProvider rsaPublicKeyProvider){
        return new JWSVerifierService(clientSettings, rsaPublicKeyProvider);
    }

}
