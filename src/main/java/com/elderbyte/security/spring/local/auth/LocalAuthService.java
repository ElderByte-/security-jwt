package com.elderbyte.security.spring.local.auth;


import com.elderbyte.security.spring.local.jwt.JwtTokenConverter;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.ParseException;

public class LocalAuthService {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenConverter tokenConverter;


    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/


    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Attempts to authenticate the current session with the given JWT string token.
     * @param jwtToken
     * @throws AuthenticationException Thrown when the authentication has failed.
     */
    public void authenticateLocal(String jwtToken) throws AuthenticationException{

        if(jwtToken == null) throw new IllegalArgumentException("jwtToken");

        try{
            SignedJWT sjwt = SignedJWT.parse(jwtToken);
            authenticateLocal(sjwt);
        }catch (ParseException e){
            throw new AuthenticationServiceException("Could not parse jwt string token!", e);
        }
    }


    /**
     * Attempts to authenticate the current session with the given JWT.
     * @throws AuthenticationException Thrown when the authentication has failed.
     */
    public void authenticateLocal(SignedJWT sjwt) throws AuthenticationException {

        if(sjwt == null) throw new IllegalArgumentException("sjwt");

        try {
            Authentication auth = authenticationManager.authenticate(tokenConverter.convert(sjwt));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (ParseException e) {
            throw new AuthenticationServiceException("Converting JWT to authentication failed!", e);
        }
    }


    /**************************************************************************
     *                                                                         *
     * Private Methods                                                         *
     *                                                                         *
     **************************************************************************/

}
