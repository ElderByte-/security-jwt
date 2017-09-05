package com.elderbyte.warden.spring.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * This authentication filter mocks a login and grants authentication automatically
 * if enabled.
 *
 * This is intended to ease development of Warden protected Clients.
 */
public class MockAuthenticationFilter extends GenericFilterBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MockJwtHolder mockJwtHolder;

    public MockAuthenticationFilter(MockJwtHolder mockJwtHolder){
        if(mockJwtHolder == null) throw new IllegalArgumentException("mockJwtHolder must not be NULL!");
        this.mockJwtHolder = mockJwtHolder;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Authentication existing = SecurityContextHolder.getContext().getAuthentication();
        mockJwtHolder.authenticateWithMock();

        if(existing != null && !(existing instanceof AnonymousAuthenticationToken)) {
            logger.warn("Existing Security-Context has been replaced by mock authentication! It was '"
                    + existing + "'");
        }
        chain.doFilter(request, response);
    }
}
