package com.elderbyte.warden.spring;

import com.elderbyte.warden.spring.mock.MockUser;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Holds the warden client authentication configuration
 */
@Configuration
@ConfigurationProperties("warden.client")
public class WardenSpringSecurityJwtSettings {

    /**
     * The realm of this client.
     *
     * - Incoming JWT tokens need to have this realm to be accepted
     * - If you use the login-proxy, this is the realm used to authenticate against
     *
     * - If a embedded running warden server is present this is the default realm.
     */
    private String realm;

    /**
     * The public RSA key which is used to verify JWT tokens.
     * This public key must be the counterpart of the authentication server's private key used to sign the token.
     *
     * - If a embedded running warden server is present it uses his own public-key.
     */
    private String publicKeyValue;

    /**
     * The warden server url. In a cloud environment, the warden service is
     * discovered by Eureka. By setting this property, Eureka is not used / required.
     */
    private String url;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
