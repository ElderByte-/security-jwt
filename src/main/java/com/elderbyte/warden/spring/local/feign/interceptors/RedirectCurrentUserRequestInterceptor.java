package com.elderbyte.warden.spring.local.feign.interceptors;

import com.elderbyte.warden.spring.local.auth.AuthenticationDetail;
import com.elderbyte.warden.spring.local.auth.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intercepts requests by adding bearer token of the current authentication context.
 * <p>
 *     Enables redirection of user authentication.
 */
public class RedirectCurrentUserRequestInterceptor extends BearerTokenRequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public String getBearerToken() {

        AuthenticationDetail auth = SecurityUtils.getAuthentication();
        if(auth != null){
            return auth.getJwtSerialized();
        }else{
            logger.warn("Could not get current user's Bearer Token since no current authentication context was found.");
            return null;
        }
    }
}