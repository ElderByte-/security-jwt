package com.elderbyte.security.spring;

import com.elderbyte.security.spring.mock.MockUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Holds the spring authentication configuration
 */
@Configuration
public class ElderSpringSecurityJwtSettings {

    /**
     * The realm of this client.
     *
     * - Incoming JWT tokens need to have this realm to be accepted
     * - May be null to omit realm check
     * - If you use the login-proxy, this is the realm used to authenticate against
     *
     */
    //@Value("#{${elder.security.jwt.realm:${warden.client.realm}} ?:null}")
    @Value("${elder.security.jwt.realm:${warden.client.realm:#{null}}}")
    private String realm;

    /**
     * The public RSA key which is used to verify JWT tokens.
     * This public key must be the counterpart of the authentication server's private key used to sign the token.
     *
     */
    @Value("${elder.security.jwt.publicKeyValue:${warden.client.publicKeyValue:#{null}}}")
    private String publicKeyValue;

    @Value("${elder.security.jwt.enableMock:${warden.client.enableMock:false}}")
    private boolean enableMock;

    @Value("${elder.security.jwt.mockUsers:${warden.client.mockUsers:#{null}}}")
    private final List<MockUser> mockUsers = new ArrayList<>();

    public Optional<String> getRealm() {
        return Optional.ofNullable(realm);
    }

    public String getPublicKeyValue() {
        return publicKeyValue;
    }

    public List<MockUser> getMockUsers() {
        return mockUsers;
    }

    public boolean isEnableMock() {
        return enableMock;
    }

}
