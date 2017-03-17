package com.elderbyte.warden.spring.local.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import({DefaultWardenWebSecurityConfiguration.class})
public class DefaultWardenSecurityConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultWardenSecurityConfig(){
        logger.info("Configuring default warden global method security...");
    }
}
