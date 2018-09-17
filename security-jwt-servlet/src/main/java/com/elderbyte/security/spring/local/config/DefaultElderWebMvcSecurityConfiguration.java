package com.elderbyte.security.spring.local.config;

import com.elderbyte.security.spring.conditions.SecurityMockDisabledCondition;
import com.elderbyte.security.spring.local.auth.LocalAuthService;
import com.elderbyte.security.spring.local.jwt.JwtAuthenticationFilter;
import com.elderbyte.security.spring.mock.MockAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.servlet.Filter;


@Configuration
@EnableWebSecurity
public class DefaultElderWebMvcSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * X-Frame-Options is a HTTP header response to prevent clickjacking.
     *
     * see https://developer.mozilla.org/de/docs/Web/HTTP/Headers/X-Frame-Options
     */
    @Value("${elder.security.web.xframeoptions:}")
    private String xFrameOptions;


    @Bean
    @Conditional(SecurityMockDisabledCondition.class)
    public Filter jwtAuthenticationFilter(LocalAuthService localAuthService){
        return new JwtAuthenticationFilter(localAuthService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        logger.info("Configuring Elder Web Security...");

        http.csrf().disable(); // Since JWTs are stored in local browser storage (if at all), we are not vulnerable to CSRF

        // For cases like usage of Iframes, we want to be able to configure X-Frame-Options.
        if (xFrameOptions.toLowerCase().equals("disable")){
            http.headers().frameOptions().disable();
        } else if (xFrameOptions.toLowerCase().equals("same_origin")) {
            http.headers().frameOptions().sameOrigin();
        }

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
