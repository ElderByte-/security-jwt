package com.elderbyte.security;

import com.elderbyte.security.mock.MockUser;
import com.elderbyte.security.spring.settings.TokenKeyProperties;

import java.util.List;
import java.util.Optional;

public interface ElderSecurityJwtSettings {

    Optional<String> getRealm();

    TokenKeyProperties getPublicKey();

    List<MockUser> getMockUsers();

    boolean isEnableMock();
}
