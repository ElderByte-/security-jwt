package com.elderbyte.security.spring.mock;

import com.elderbyte.security.mock.MockJwtHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This authentication filter mocks a login and grants authentication automatically
 * if enabled.
 *
 * This is intended to ease development of Warden protected Clients.
 */
@Order(100)
public class MockAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MockJwtHolder mockJwtHolder;

    public MockAuthenticationFilter(MockJwtHolder mockJwtHolder){
        if(mockJwtHolder == null) throw new IllegalArgumentException("mockJwtHolder must not be NULL!");
        this.mockJwtHolder = mockJwtHolder;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Authentication existing = SecurityContextHolder.getContext().getAuthentication();

        logger.debug("Authenticating request with mock credentials! " + request.getRequestURI());
        mockJwtHolder.authenticateWithMock();

        if(existing != null && !(existing instanceof AnonymousAuthenticationToken)) {
            logger.warn("Existing Security-Context has been replaced by mock authentication! It was '"
                    + existing + "'");
        }
        chain.doFilter(request, response);
    }
}
