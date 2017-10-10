package com.elderbyte.warden.spring.local.auth;

import org.springframework.security.access.AccessDeniedException;

import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 * Access restrictions expression language.
 *
 */
public abstract class Acw {

    /**
     * Generic predicate which can be used to check a Authentication detail
     */
    public static Acw has(Predicate<AuthenticationDetail> auth){
        return dynamicEval(() -> SecurityUtils.hasAuthWith(auth));
    }

    /**
     * The current user must be in the given realm
     * @param realmId The realm to check
     */
    public static Acw hasRealm(String realmId){
        return dynamicEval(() -> SecurityUtils.hasRealm(realmId));
    }

    /**
     * The current user must be in at least one of the given realms
     * @param realms Ids of the realms
     */
    public static Acw hasAnyRealm(String... realms) {
        return dynamicEval(() -> SecurityUtils.hasAnyRealm(realms));
    }

    /**
     * The current user must only have one of the given roles
     */
    public static Acw hasAnyRole(String... roles){
        return dynamicEval(() -> SecurityUtils.hasAnyRole(roles));
    }

    /**
     * The current user must have requireAll given roles
     */
    public static Acw hasRoles(String... roles){
        return dynamicEval(() -> SecurityUtils.hasAllRoles(roles));
    }

    /**
     * The current user must have requireAll given roles
     */
    public static Acw hasRole(String role){
        return dynamicEval(() -> SecurityUtils.hasRole(role));
    }

    /**
     * Evals to true
     */
    public static Acw grant(){
        return dynamicEval(() -> true);
    }

    /**
     * Evals to false
     */
    public static Acw deny(){
        return dynamicEval(() -> false);
    }

    public static Acw dynamicEval(Supplier<Boolean> expr){
        return new AcwDynamic(expr);
    }


    /**
     * Only one of the given conditions is required to evalPermission true.
     */
    public static Acw requireAny(Acw... acws){
        return dynamicEval(() -> {
            for (Acw c : acws) {
                if(c.evalPermission()) return true;
            }
            return false;
        });
    }

    /**
     * All of the given conditions is required to evalPermission true.
     */
    public static Acw requireAll(Acw... acws){
        return dynamicEval(() -> {
            for (Acw c : acws) {
                if(!c.evalPermission()) return false;
            }
            return true;
        });
    }


    /**
     * Returns true if the given access rights expression is matched.
     */
    public abstract boolean evalPermission();

    /**
     * Checks the permission expression and throws a AccessDeniedException
     * if failed
     * @throws AccessDeniedException
     */
    public void enforce(){
        if(!this.evalPermission()){
            throw new AccessDeniedException("User has not the required permission!");
        }
    }


    // Inner classes for the expression tree

    private static final class AcwDynamic extends Acw {

        private Supplier<Boolean> dynamic;

        AcwDynamic(Supplier<Boolean> dynamic){
            this.dynamic = dynamic;
        }

        @Override
        public boolean evalPermission() {
            return dynamic.get();
        }
    }

}
