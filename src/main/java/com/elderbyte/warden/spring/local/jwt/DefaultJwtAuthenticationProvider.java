package com.elderbyte.warden.spring.local.jwt;
import com.elderbyte.warden.spring.local.auth.AuthenticationDetail;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


/**
 * A default implementation which supports basic JWT authentication.
 */
public class DefaultJwtAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JWSVerifierService jwsVerifierService;


    public DefaultJwtAuthenticationProvider(JWSVerifierService jwsVerifierService){
        this.jwsVerifierService = jwsVerifierService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        AuthenticationDetail auth = (AuthenticationDetail)authentication;
        SignedJWT token = (SignedJWT) auth.getDetails();

        jwsVerifierService.verifyOrThrow(token);

        // The token has a valid signature (we can trust its content)
        checkVerifiedToken(token);

        auth.setAuthenticated(true);

        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationDetail.class.isAssignableFrom(authentication);
    }



    /**
     * Checks if the token is valid (expiration date and co.) (its signature has already been checked)
     * @param token the token to check
     * @throws JwtAuthenticationException If the token is not accepted
     */
    protected void checkVerifiedToken(SignedJWT token) throws JwtAuthenticationException{
        try {
            Date expirationDate = token.getJWTClaimsSet().getExpirationTime();
            if(expirationDate != null){
                if(LocalDateTime.now().isAfter(toDateTime(expirationDate))){
                    // The token has expired!
                    throw new JwtAuthenticationException("Authentication failed - The provided token is valid but already expired!");
                }
            }

            Date notValidBefore = token.getJWTClaimsSet().getNotBeforeTime();
            if(notValidBefore != null){
                if(LocalDateTime.now().isBefore(toDateTime(notValidBefore))){
                    // The token is not yet valid!
                    throw new JwtAuthenticationException("Authentication failed - The provided token is valid not yet valid (notValidBefore is set to " + toDateTime(notValidBefore).toString() + "!");
                }
            }
        }catch (ParseException e){
            throw new JwtAuthenticationException("Authentication failed - Parsing claims failed.", e);
        }
    }


    private static LocalDateTime toDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

}
