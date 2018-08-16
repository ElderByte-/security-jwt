package com.elderbyte.security.spring;

import com.elderbyte.security.spring.mock.MockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Holds the spring authentication configuration
 */
public class ElderSpringSecurityJwtSettings {

    private final ElderSpringSecurityJwtProperties elderSpringSecurityJwtProperties;
    private final LegacySpringSecurityJwtProperties legacySpringSecurityJwtProperties;

    public ElderSpringSecurityJwtSettings(
            ElderSpringSecurityJwtProperties elderSpringSecurityJwtProperties,
            LegacySpringSecurityJwtProperties legacySpringSecurityJwtProperties
            ){
        this.elderSpringSecurityJwtProperties = elderSpringSecurityJwtProperties;
        this.legacySpringSecurityJwtProperties = legacySpringSecurityJwtProperties;
    }

    public Optional<String> getRealm() {

        var realm = Optional.ofNullable(elderSpringSecurityJwtProperties.getRealm());

        if(realm.isPresent()){
            return realm;
        }else {
            return Optional.ofNullable(legacySpringSecurityJwtProperties.getRealm());
        }
    }

    public String getPublicKeyValue() {
        var pubkey = elderSpringSecurityJwtProperties.getPublicKeyValue();
        if(pubkey == null){
            return legacySpringSecurityJwtProperties.getPublicKeyValue();
        }else{
            return pubkey;
        }
    }

    public List<MockUser> getMockUsers() {
        if(elderSpringSecurityJwtProperties.isEnableMock()){
            return elderSpringSecurityJwtProperties.getMockUsers();
        }
        return legacySpringSecurityJwtProperties.getMockUsers();
    }

    public boolean isEnableMock() {
        return elderSpringSecurityJwtProperties.isEnableMock() || legacySpringSecurityJwtProperties.isEnableMock();
    }

}
