package com.elderbyte.security.spring;

import org.springframework.boot.WebApplicationType;

public class ServletWebApplicationCondition extends WebApplicationTypeCondition {
    public ServletWebApplicationCondition() {
        super(WebApplicationType.SERVLET);
    }
}
