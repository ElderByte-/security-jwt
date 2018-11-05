package com.elderbyte.security.spring.settings;

import com.elderbyte.security.ElderSecurityJwtSettings;
import com.elderbyte.security.mock.MockUser;
import java.util.List;
import java.util.Optional;

/**
 * Holds the spring authentication configuration
 */
public class ElderSecurityJwtSettingsFallback implements ElderSecurityJwtSettings {

    private final ElderSpringSecurityJwtProperties elderSpringSecurityJwtProperties;
    private final LegacySpringSecurityJwtProperties legacySpringSecurityJwtProperties;


    public ElderSecurityJwtSettingsFallback(
            ElderSpringSecurityJwtProperties elderSpringSecurityJwtProperties
    ){
        this(elderSpringSecurityJwtProperties, null);
    }

    public ElderSecurityJwtSettingsFallback(
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

    public TokenKeyProperties getPublicKey() {
        var pubkey = elderSpringSecurityJwtProperties.getPublicKey();
        if(pubkey == null){
            return legacySpringSecurityJwtProperties.getPublicKey();
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
