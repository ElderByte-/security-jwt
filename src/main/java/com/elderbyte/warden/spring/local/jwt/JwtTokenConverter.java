package com.elderbyte.warden.spring.local.jwt;

import com.elderbyte.warden.spring.local.auth.AuthenticationDetail;
import com.nimbusds.jwt.JWT;

import java.text.ParseException;

/**
 * Provides the ability to convert JWT tokens into Authentication Objects
 */
public interface JwtTokenConverter {

    /**
     * Converts the given token into an authentication by parsing its claims.
     * @param jwt The token to convert
     * @return A populated AuthenticationDetail
     * @throws ParseException If the token was malformed and could not be read.
     */
    AuthenticationDetail convert(JWT jwt) throws ParseException;

}
