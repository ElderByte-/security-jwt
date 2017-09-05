package com.elderbyte.warden.spring.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            mockJwtHolder.authenticateWithMock();
        }
        else {
            if (logger.isDebugEnabled()) {
                logger.debug("SecurityContextHolder not populated with mock authentication, as it already contained: '"
                        + SecurityContextHolder.getContext().getAuthentication() + "'");
            }
        }
        chain.doFilter(request, response);
    }
}
