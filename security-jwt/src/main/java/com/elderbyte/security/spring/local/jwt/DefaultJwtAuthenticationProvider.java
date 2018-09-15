package com.elderbyte.security.spring.local.jwt;
import com.elderbyte.security.spring.local.auth.AuthenticationDetail;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


/**
 * A default implementation which supports basic JWT authentication.
 */
public class DefaultJwtAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtValidationService jwtValidationService;


    public DefaultJwtAuthenticationProvider(JwtValidationService jwtValidationService){
        this.jwtValidationService = jwtValidationService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        AuthenticationDetail auth = (AuthenticationDetail)authentication;
        SignedJWT token = (SignedJWT) auth.getDetails();

        jwtValidationService.validOrThrow(token);
        auth.setAuthenticated(true);

        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationDetail.class.isAssignableFrom(authentication);
    }
}
