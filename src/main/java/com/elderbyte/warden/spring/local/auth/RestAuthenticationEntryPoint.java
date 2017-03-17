package com.elderbyte.warden.spring.local.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Spring will send all unauthorized requests to the AuthenticationEntryPoint.
 * In REST we just can return a 401.
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response, AuthenticationException authException)
        throws IOException, ServletException {
        System.out.println("JwtAuthenticationException.commence");
        response.addHeader("X-Unauthorized", "2");
        response.setStatus(401);
    }
}
