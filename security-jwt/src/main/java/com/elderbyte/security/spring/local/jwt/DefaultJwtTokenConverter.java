package com.elderbyte.security.spring.local.jwt;

import com.elderbyte.security.spring.local.auth.AuthenticationDetail;
import com.elderbyte.security.spring.local.auth.AuthenticationDetailImpl;
import com.elderbyte.security.spring.local.auth.AuthorityUtil;
import com.nimbusds.jwt.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import java.text.ParseException;
import java.util.*;

import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Default implementation of a JWT to Authentication Converter.
 * Supports the standard JWT claims and additionally "name" and "roles".
 *
 * Ensures that roles have the ROLE_* prefix, as required by Spring security.
 */
public class DefaultJwtTokenConverter implements JwtTokenConverter {

    private static final Logger logger = LoggerFactory.getLogger(DefaultJwtTokenConverter.class);

    /**
     * Builds an authentication detail from the given signed JWT
     * @param sjwt The JWT to convert
     * @return The authentication extracted from the JWT
     */
    @Override
    public AuthenticationDetail convert(JWT sjwt) throws ParseException {
        JWTClaimsSet claims = sjwt.getJWTClaimsSet();

        Set<GrantedAuthority> authorities = getAuthorities(claims);

        return new AuthenticationDetailImpl(
                getRealm(claims),
                claims.getStringClaim("username"),
                claims.getSubject(),
                claims.getStringClaim("name"),
                authorities,
                sjwt);
    }


    private String getRealm(JWTClaimsSet claims) throws ParseException {
        String realm = claims.getStringClaim("realm");
        if(realm == null || realm.isEmpty()){
            // Fall back to legacy handling, attempt to extract realm from audience claim
            // TODO Remove Legacy handling
            realm = (claims.getAudience() != null && !claims.getAudience().isEmpty()) ? claims.getAudience().get(0) : null;
        }
        return realm;
    }


    @SuppressWarnings("unchecked")
    private Set<GrantedAuthority> getAuthorities(JWTClaimsSet claims) {

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        Object rolesObj = claims.getClaim("roles");


        if(rolesObj instanceof Iterable<?>){

            Iterator<?> itemIterator = ((Iterable<?>)rolesObj).iterator();
            if(itemIterator.hasNext()){

                Object item = itemIterator.next();

                if(item instanceof String){
                    grantedAuthorities.addAll(AuthorityUtil.createAuthorities((Iterable<String>) rolesObj));
                }else{
                    logger.warn("Unexpected role object - expected string but got " + item.getClass());
                }
            }

        }else if( rolesObj instanceof String){
            grantedAuthorities.addAll(AuthorityUtil.parseAuthorities((String) rolesObj));
        }else{
            logger.warn("Unknown data type in JWT for roles-claim: " + (rolesObj != null ? rolesObj.getClass() : "<null>"));
        }

        return grantedAuthorities;
    }




}
