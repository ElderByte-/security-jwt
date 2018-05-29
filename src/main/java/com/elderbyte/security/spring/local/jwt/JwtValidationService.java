package com.elderbyte.security.spring.local.jwt;

import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Checks if a JWT is valid. This includes the signature and the exiraption dates.
 */
public class JwtValidationService {

    private JWSVerifierService jwsVerifierService;

    public JwtValidationService(JWSVerifierService jwsVerifierService){
        this.jwsVerifierService = jwsVerifierService;
    }

    /**
     * Checks if a JWT is valid. This includes the signature and the exiraption dates.
     * If invalid, throws a JwtAuthenticationException.
     */
    public void validOrThrow(SignedJWT jwt) throws JwtAuthenticationException {
        jwsVerifierService.verifyOrThrow(jwt);
        checkVerifiedToken(jwt);
    }


    /**
     * Checks if the token is valid (expiration date and co.) (its signature has already been checked)
     * @param token the token to check
     * @throws JwtAuthenticationException If the token is not accepted
     */
    private void checkVerifiedToken(SignedJWT token) throws JwtAuthenticationException{
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
