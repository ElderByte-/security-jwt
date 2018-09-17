package com.elderbyte.security.spring.mock;

import com.elderbyte.security.mock.MockJwtHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * This authentication filter mocks a login and grants authentication automatically
 * if enabled.
 *
 * This is intended to ease development of Warden protected Clients.
 */
@Order(100)
public class MockAuthenticationFilter implements WebFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MockJwtHolder mockJwtHolder;

    public MockAuthenticationFilter(MockJwtHolder mockJwtHolder){
        if(mockJwtHolder == null) throw new IllegalArgumentException("mockJwtHolder must not be NULL!");
        this.mockJwtHolder = mockJwtHolder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange,
                             WebFilterChain webFilterChain) {

        var request = serverWebExchange.getRequest();

        Authentication existing = SecurityContextHolder.getContext().getAuthentication();

        logger.debug("Authenticating request with mock credentials! " + request.getURI());
        mockJwtHolder.authenticateWithMock();

        if(existing != null && !(existing instanceof AnonymousAuthenticationToken)) {
            logger.warn("Existing Security-Context has been replaced by mock authentication! It was '"
                    + existing + "'");
        }

        return webFilterChain.filter(serverWebExchange);
    }

}
