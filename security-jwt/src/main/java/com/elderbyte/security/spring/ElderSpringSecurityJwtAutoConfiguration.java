package com.elderbyte.security.spring;

import com.elderbyte.security.ElderSecurityJwtSettings;
import com.elderbyte.security.spring.local.auth.LocalAuthService;
import com.elderbyte.security.spring.mock.ElderSecurityMockConfiguration;
import com.elderbyte.security.spring.settings.ElderSpringSecurityJwtSettingsConfig;
import com.elderbyte.security.spring.local.feign.DefaultFeignSecurityConfiguration;
import com.elderbyte.security.spring.local.jwt.*;
import com.elderbyte.security.rsa.DefaultRSAPublicKeyProvider;
import com.elderbyte.security.rsa.RSAPublicKeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
@EnableConfigurationProperties
@Import({
        ElderSpringSecurityJwtSettingsConfig.class,
        DefaultFeignSecurityConfiguration.class,
        ElderSecurityMockConfiguration.class
})
public class ElderSpringSecurityJwtAutoConfiguration {

    @Autowired
    private ElderSecurityJwtSettings clientSettings;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(JwtValidationService validationService){
        return new DefaultJwtAuthenticationProvider(validationService);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationProvider provider){
        return  new ProviderManager(Collections.singletonList(provider));
    }

    @Bean
    public LocalAuthService LocalAuthService(AuthenticationManager authenticationManager, JwtTokenConverter tokenConverter) {
        return new LocalAuthService(authenticationManager, tokenConverter);
    }

    @Bean
    @ConditionalOnMissingBean(JwtTokenConverter.class)
    public JwtTokenConverter tokenConverter(){
        return new DefaultJwtTokenConverter();
    }

    @Bean
    @ConditionalOnMissingBean(RSAPublicKeyProvider.class)
    public RSAPublicKeyProvider RSAPublicKeyProvider(){
        return new DefaultRSAPublicKeyProvider(clientSettings);
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