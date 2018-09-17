package com.elderbyte.security.spring.local.config;

import com.elderbyte.security.spring.conditions.SecurityMockDisabledCondition;
import com.elderbyte.security.spring.local.auth.LocalAuthService;
import com.elderbyte.security.spring.local.jwt.JwtAuthenticationReactiveWebFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.web.server.WebFilter;


@Configuration
@EnableWebFluxSecurity
public class DefaultElderWebFluxSecurityConfiguration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * X-Frame-Options is a HTTP header response to prevent clickjacking.
     *
     * see https://developer.mozilla.org/de/docs/Web/HTTP/Headers/X-Frame-Options
     */
    @Value("${elder.security.web.xframeoptions:}")
    private String xFrameOptions;



    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(AuthenticationManager authenticationManager) {
        return new ReactiveAuthenticationManagerAdapter(authenticationManager);
    }

    @Bean
    @Conditional(SecurityMockDisabledCondition.class)
    public WebFilter jwtAuthenticationFilter(LocalAuthService localAuthService){
        return new JwtAuthenticationReactiveWebFilter(localAuthService);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager) {

        http.httpBasic().disable();
        http.formLogin().disable();

        http.csrf().disable();

        http.authenticationManager(authenticationManager);

        // For cases like usage of Iframes, we want to be able to configure X-Frame-Options.
        if (xFrameOptions.toLowerCase().equals("disable")){
            http.headers().frameOptions().disable();
        } else if (xFrameOptions.toLowerCase().equals("same_origin")) {
            http.headers().frameOptions()
                    .mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN);
        }

        return http.build();
    }
}
