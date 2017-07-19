package com.elderbyte.warden.spring.local;

import com.elderbyte.warden.spring.WardenSpringSecurityJwtSettings;
import com.elderbyte.warden.spring.local.auth.LocalAuthService;
import com.elderbyte.warden.spring.local.config.AccessDeniedExceptionHandler;
import com.elderbyte.warden.spring.local.config.DefaultWardenSecurityConfig;
import com.elderbyte.warden.spring.local.auth.RestAuthenticationEntryPoint;
import com.elderbyte.warden.spring.local.jwt.*;
import com.elderbyte.warden.spring.rsa.PropertyRSAPublicKeyProvider;
import com.elderbyte.warden.spring.rsa.RSAPublicKeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableConfigurationProperties
@Import({
        WardenSpringSecurityJwtSettings.class,
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
    public AuthenticationProvider authenticationProvider(JwtValidationService validationService){
        return new DefaultJwtAuthenticationProvider(validationService);
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
    @ConditionalOnMissingBean(RSAPublicKeyProvider.class)
    public RSAPublicKeyProvider RSAPublicKeyProvider(){
        return new PropertyRSAPublicKeyProvider(clientSettings);
    }

    @Bean
    @ConditionalOnMissingBean(JWSVerifierService.class)
    public JWSVerifierService jwsVerifierService(RSAPublicKeyProvider rsaPublicKeyProvider){
        return new JWSVerifierService(clientSettings, rsaPublicKeyProvider);
    }

    @Bean
    @ConditionalOnMissingBean(JwtValidationService.class)
    public JwtValidationService jwsVerifierService(JWSVerifierService jwsVerifierService){
        return new JwtValidationService(jwsVerifierService);
    }
}
