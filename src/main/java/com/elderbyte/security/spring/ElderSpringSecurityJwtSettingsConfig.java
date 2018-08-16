package com.elderbyte.security.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElderSpringSecurityJwtSettingsConfig {


    @Bean
    public ElderSpringSecurityJwtProperties elderSpringSecurityJwtProperties(){
        return new ElderSpringSecurityJwtProperties();
    }

    @Bean
    public LegacySpringSecurityJwtProperties legacySpringSecurityJwtProperties(){
        return new LegacySpringSecurityJwtProperties();
    }

    @Bean
    public ElderSpringSecurityJwtSettings elderSpringSecurityJwtSettings(){
        return new ElderSpringSecurityJwtSettings(elderSpringSecurityJwtProperties(), legacySpringSecurityJwtProperties());
    }

}
