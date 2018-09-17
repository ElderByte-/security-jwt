package com.elderbyte.security.spring.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 *  * MOCK DISABLED
 *  * This condition evaluates to true if the security should NOT be mocked.
 */
public class SecurityMockDisabledCondition implements Condition {

    private SecurityMockEnabledCondition mockEnabledCondition = new SecurityMockEnabledCondition();

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !mockEnabledCondition.matches(context, metadata);
    }
}
