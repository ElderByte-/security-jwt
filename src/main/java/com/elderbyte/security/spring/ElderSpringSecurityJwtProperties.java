package com.elderbyte.security.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("elder.security.jwt")
public class ElderSpringSecurityJwtProperties extends SecurityJwtProperties {

}
