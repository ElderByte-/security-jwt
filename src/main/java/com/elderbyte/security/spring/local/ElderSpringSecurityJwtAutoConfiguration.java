package com.elderbyte.security.spring.local;

import com.elderbyte.security.spring.ElderSpringSecurityJwtSettings;
import com.elderbyte.security.spring.local.auth.LocalAuthService;
import com.elderbyte.security.spring.local.config.AccessDeniedExceptionHandler;
import com.elderbyte.security.spring.local.config.DefaultElderSecurityConfig;
import com.elderbyte.security.spring.local.auth.RestAuthenticationEntryPoint;
import com.elderbyte.security.spring.local.feign.DefaultFeignSecurityConfiguration;
import com.elderbyte.security.spring.local.jwt.*;
import com.elderbyte.security.spring.mock.ElderSpringSecurityMockConfiguration;
import com.elderbyte.security.spring.rsa.PropertyRSAPublicKeyProvider;
import com.elderbyte.security.spring.rsa.RSAPublicKeyProvider;
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
        ElderSpringSecurityMockConfiguration.class,
        ElderSpringSecurityJwtSettings.class,
        AccessDeniedExceptionHandler.class,
        DefaultElderSecurityConfig.class,
        DefaultFeignSecurityConfiguration.class
})
public class ElderSpringSecurityJwtAutoConfiguration {

    @Autowired
    private ElderSpringSecurityJwtSettings clientSettings;

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
    public JwtValidationService jwtValidationService(JWSVerifierService jwsVerifierService){
        return new JwtValidationService(jwsVerifierService);
    }
}
