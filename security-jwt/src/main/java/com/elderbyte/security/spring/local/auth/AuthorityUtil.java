package com.elderbyte.security.spring.local.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility to create authorities / parse them from string roles.
 */
public final class AuthorityUtil {


    public static Set<GrantedAuthority> createAuthorities(Iterable<String> roles){

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (String role : roles) {
            if(!role.isEmpty()) {
                authorities.add(createAuthority(role));
            }
        }

        return authorities;
    }

    public static Set<GrantedAuthority> createAuthorities(String[] roles){
        return createAuthorities(Arrays.asList(roles));
    }

    public static Set<GrantedAuthority> parseAuthorities(String rolesString){

        Set<GrantedAuthority> authorities = new HashSet<>();

        if (!rolesString.isEmpty()) {
            String[] roles = rolesString.split(",");
            createAuthorities(roles);
        }
        return authorities;
    }


    public static SimpleGrantedAuthority createAuthority(String role){

        // Spring security demands that Authorities which act as roles
        // should be prefixed with "ROLE_", see @RoleVoter.
        // So if the roles in the JWT are not prefixed with 'ROLE_', dynamically prefix them now:

        if(!role.startsWith("ROLE_")){
            role = "ROLE_" + role;
        }
        return new SimpleGrantedAuthority(role);

    }

}
