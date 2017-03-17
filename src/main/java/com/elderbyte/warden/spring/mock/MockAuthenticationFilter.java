package com.elderbyte.warden.spring.mock;

import com.elderbyte.warden.spring.WardenSpringSecurityJwtSettings;
import com.elderbyte.warden.spring.local.auth.AuthenticationDetail;
import com.elderbyte.warden.spring.local.auth.AuthenticationDetailImpl;
import com.elderbyte.warden.spring.local.auth.AuthorityUtil;
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

    private final WardenSpringSecurityJwtSettings clientSettings;
    private final MockJwtHolder mockJwtHolder;

    public MockAuthenticationFilter(WardenSpringSecurityJwtSettings clientSettings, MockJwtHolder mockJwtHolder){

        if(clientSettings == null) throw new IllegalArgumentException("clientSettings");
        if(mockJwtHolder == null) throw new IllegalArgumentException("mockJwtHolder");

        this.clientSettings = clientSettings;
        this.mockJwtHolder = mockJwtHolder;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {


            if(!clientSettings.getMockUsers().isEmpty()){
                MockUser mockUser = clientSettings.getMockUsers().get(0);

                AuthenticationDetail auth = new AuthenticationDetailImpl(
                        mockUser.getRealm(),
                        mockUser.getLogin(),
                        mockUser.getFullName(),
                        AuthorityUtil.createAuthorities(mockUser.getRoles()),
                        mockJwtHolder.getSignedJWTMock(mockUser)
                );
                auth.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(auth);

                logger.warn("SecurityContextHolder populated with mock authentication. For development purposes only!");
            }else{
                logger.warn("Cant inject mock user into auth since no mock user was defined!");
            }
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
