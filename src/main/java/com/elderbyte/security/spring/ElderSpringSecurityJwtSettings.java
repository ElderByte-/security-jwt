package com.elderbyte.security.spring;

import com.elderbyte.security.spring.mock.MockUser;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Holds the spring authentication configuration
 */
@Configuration
@ConfigurationProperties("elder.security.jwt")
public class ElderSpringSecurityJwtSettings {

    /**
     * The realm of this client.
     *
     * - Incoming JWT tokens need to have this realm to be accepted
     * - May be null to omit realm check
     * - If you use the login-proxy, this is the realm used to authenticate against
     *
     */
    private String realm;

    /**
     * The public RSA key which is used to verify JWT tokens.
     * This public key must be the counterpart of the authentication server's private key used to sign the token.
     *
     */
    private String publicKeyValue;


    private boolean enableMock;
    private final List<MockUser> mockUsers = new ArrayList<>();

    public Optional<String> getRealm() {
        return Optional.ofNullable(realm);
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getPublicKeyValue() {
        return publicKeyValue;
    }

    public void setPublicKeyValue(String publicKeyValue) {
        this.publicKeyValue = publicKeyValue;
    }

    public List<MockUser> getMockUsers() {
        return mockUsers;
    }

    public boolean isEnableMock() {
        return enableMock;
    }

    public void setEnableMock(boolean enableMock) {
        this.enableMock = enableMock;
    }

}
