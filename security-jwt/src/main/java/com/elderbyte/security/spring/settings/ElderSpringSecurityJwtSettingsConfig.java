package com.elderbyte.security.spring.settings;

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
    public ElderSecurityJwtSettingsFallback elderSpringSecurityJwtSettings(){
        return new ElderSecurityJwtSettingsFallback(elderSpringSecurityJwtProperties(), legacySpringSecurityJwtProperties());
    }

}
