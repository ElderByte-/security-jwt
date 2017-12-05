package com.elderbyte.warden.spring.local.config;

import com.elderbyte.warden.spring.local.auth.LocalAuthService;
import com.elderbyte.warden.spring.local.jwt.JwtAuthenticationFilter;
import com.elderbyte.warden.spring.mock.MockAuthenticationFilter;
import com.elderbyte.warden.spring.mock.MockJwtHolder;
import com.elderbyte.warden.spring.WardenSpringSecurityJwtSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class DefaultWardenWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LocalAuthService localAuthService;

    @Autowired
    private WardenSpringSecurityJwtSettings clientSettings;

    @Autowired(required = false)
    private MockJwtHolder mockJwtHolder;


    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        logger.info("Configuring Warden Web Security...");

        http.csrf().disable(); // Since JWTs are stored in local browser storage (if at all), we are not vulnerable to CSRF

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(new JwtAuthenticationFilter(localAuthService), AnonymousAuthenticationFilter.class);


        if (clientSettings.isEnableMock()){
            if(mockJwtHolder != null){
                logger.info("Enabling mock user authentication filter!");
                http.addFilterAfter(new MockAuthenticationFilter(mockJwtHolder), JwtAuthenticationFilter.class);
            }else{
                throw new IllegalStateException("Cant configure mock filter because mock service 'mockJwtHolder' is not available! This looks like a configuration issue.");
            }
        }
    }
}
