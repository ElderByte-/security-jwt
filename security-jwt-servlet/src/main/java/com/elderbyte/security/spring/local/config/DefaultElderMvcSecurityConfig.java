package com.elderbyte.security.spring.local.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import({DefaultElderWebMvcSecurityConfiguration.class})
public class DefaultElderMvcSecurityConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultElderMvcSecurityConfig(){
        logger.info("Configuring default elder global method security...");
    }
}
