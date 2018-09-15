package com.elderbyte.security;

import com.elderbyte.security.mock.MockUser;

import java.util.List;
import java.util.Optional;

public interface ElderSecurityJwtSettings {

    Optional<String> getRealm();

    String getPublicKeyValue();

    List<MockUser> getMockUsers();

    boolean isEnableMock();
}
