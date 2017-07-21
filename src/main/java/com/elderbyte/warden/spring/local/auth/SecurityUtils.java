package com.elderbyte.warden.spring.local.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Provides easy access to spring security.
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Returns the current authentication details.
     */
    public static AuthenticationDetail getAuthentication(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            if(auth instanceof AuthenticationDetail){
                return (AuthenticationDetail)auth;
            }else{
                throw new IllegalStateException("There was an Authentication in the Security-Context but it does not support 'AuthenticationDetail'. " + auth);
            }
        }
        return null;
    }

    /**
     * Get the login of the current user.
     */
    public static String getCurrentLogin() {
        AuthenticationDetail auth = getAuthentication();
        if(auth != null){
            return auth.getLoginName();
        }
        return null;
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated();
    }


    public static boolean hasAuthWith(Predicate<AuthenticationDetail> authPredicate) {
        AuthenticationDetail auth = getAuthentication();
        return auth != null && authPredicate.test(auth);
    }


    public static boolean hasLogin(String login){
        return Objects.equals(login, getCurrentLogin());
    }


    /**
     * If the current user has a specific security role.
     */
    public static boolean hasRole(String role) {
        return hasAuthWith(auth -> auth.hasRole(role));
    }

    public static boolean hasAnyRole(String... roles) {
        return hasAuthWith(auth -> {
            for (String role : roles) {
                if(auth.hasRole(role)) {
                    return true;
                }
            }
            return false;
        });
    }


    public static boolean hasAllRoles(String... roles) {
        return hasAuthWith(auth -> {
            for (String role : roles) {
                if(!auth.hasRole(role)) return false;
            }
            return true;
        });
    }



    public static Boolean hasRealm(String realmId) {
        return hasAuthWith(auth -> Objects.equals(realmId, auth.getRealm()));
    }


    public static Boolean hasAnyRealm(String... realms) {
        for (String realm : realms) {
            if(hasRealm(realm))return true;
        }
        return false;
    }
}
