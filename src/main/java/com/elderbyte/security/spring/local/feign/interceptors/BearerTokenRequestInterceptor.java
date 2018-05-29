package com.elderbyte.security.spring.local.feign.interceptors;

import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intercepts web requests by adding bearer token.
 */
public abstract class BearerTokenRequestInterceptor implements AuthRequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void apply(RequestTemplate template) {

        String bearerToken = getBearerToken();
        if(bearerToken != null){
            logger.trace("Injecting Bearer Token to Authorization header.");
            template.header("Authorization", "bearer " + bearerToken);
        }else{
            logger.warn("Redirecting Bearer Token not possible since no current Bearer Token is available.");
        }
    }

    public abstract String getBearerToken();

}