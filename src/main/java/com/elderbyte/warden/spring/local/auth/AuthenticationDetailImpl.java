package com.elderbyte.warden.spring.local.auth;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents the details of an authenticated user.
 *
 * Immutable implementation
 */
public class AuthenticationDetailImpl implements AuthenticationDetail {

    private final Set<? extends GrantedAuthority> authorities;
    private final Set<String> roles;
    private final String realm;
    private final String fullName;
    private final String subject;
    private final Object details;
    private final String username;

    private boolean isAuthenticated = false;


    /**
     * Creates a new AuthenticationDetail
     * @param realm The security realm / tenant
     * @param username The user name
     * @param subject The unique user id
     * @param fullName A user friendly display string
     * @param authorities The security roles
     * @param details Additional, untyped details
     */
    public AuthenticationDetailImpl(
            String realm,
            String username,
            String subject,
            String fullName,
            Set<? extends GrantedAuthority> authorities,
            Object details){
        this.realm = realm;
        this.username = username;
        this.subject = subject;
        this.authorities = Collections.unmodifiableSet(authorities);
        this.roles = authorities.stream().map(a -> a.getAuthority()).collect(Collectors.toSet());
        this.fullName = fullName;
        this.details = details;
    }

    /**
     * Gets the security context (realm)
     */
    @Override
    public String getRealm() {
        return realm;
    }


    @Override
    public String getSubject() {
        return getPrincipal().toString();
    }

    @Override
    public String getUserName() {
        return username;
    }

    /**
     * Gets the full display name of the user / subject
     */
    @Override
    public String getName() {
        return fullName;
    }

    /**
     * Gets a identifier of the current subject.
     * A subject is guaranteed to be unique at least within a realm.
     */
    @Override
    public Object getPrincipal() {
        return subject;
    }

    /**
     * Returns the granted roles / authorities.
     * Note that the set is read-only.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Not used, will always return an empty string
     */
    @Override
    public Object getCredentials() {
        return "";
    }


    /**
     * Returns true if this user is authenticated.
     */
    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }


    /**
     * Not used.
     *
     * Returns details, usually only relevant for debugging.
     * In case of token authentication, may return a dump of the token.
     *
     * */
    @Override
    public Object getDetails() {
        return details;
    }

    /**
     * Marks as valid auth
     * @throws IllegalArgumentException
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String toString(){
        return getSubject() + " (" + getName() + ") " + getRolesString();
    }

    private String getRolesString(){
        String roles = getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(","));
        return  "[" + roles +"]";
    }


    @Override // Overridden for better performance
    public boolean hasRole(String role) {
        return roles.contains(role);
    }


}
