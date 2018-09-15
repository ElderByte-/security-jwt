package com.elderbyte.security.spring.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;


@Deprecated
@ConfigurationProperties("warden.client")
public class LegacySpringSecurityJwtProperties extends SecurityJwtProperties {

}
