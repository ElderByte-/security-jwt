package com.elderbyte.security.spring.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * MOCK ENABLED
 * This condition evaluates to true if the security should be mocked.
 */
public class SecurityMockEnabledCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        var mockEnabled = context.getEnvironment().getProperty("elder.security.jwt.enableMock");
        var mockEnabledLegacy = context.getEnvironment().getProperty("warden.client.enableMock");

        if(mockEnabled != null && mockEnabled.equals("true")){
            return true;
        }

        if(mockEnabledLegacy != null && mockEnabledLegacy.equals("true")){
            return true;
        }

        return false;
    }
}
