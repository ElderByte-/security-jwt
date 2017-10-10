package com.elderbyte.warden.spring.local.auth;

import com.nimbusds.jwt.JWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;



/**
 * Enhances the default authentication with useful details.
 */
public interface AuthenticationDetail extends Authentication {

    /**
     * Gets the security realm of this user.
     */
    String getRealm();

    /**
     * Gets the user identity, which must be unique in the current environment,
     * even over multiple realms.
     *
     * It can be an internal id, uuid, or a natural id like email etc.
     *
     * This is a short-hand for the generic getPrincipal()
     */
    String getSubject();

    /**
     * Checks if the given Principal has the given role
     * @param role The role to check
     * @return True if the given role is present in this authenticaiton
     */
    default boolean hasRole(String role) {
        return getAuthorities().contains(new SimpleGrantedAuthority(role));
    }

    /**
     * Returns the JWT attached to this request.
     */
    default JWT getJwt(){
        if (this.getDetails() instanceof JWT) {
            return ((JWT) this.getDetails());
        }else{
            throw new IllegalStateException("The current AuthenticationDetail does not hold a JWT in details property!");
        }
    }

    /**
     * Returns the serialized JWT (bearer token format)
     */
    default String getJwtSerialized(){
        return getJwt().serialize();
    }


}
