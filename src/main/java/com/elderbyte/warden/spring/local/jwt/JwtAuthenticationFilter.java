package com.elderbyte.warden.spring.local.jwt;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.elderbyte.warden.spring.local.auth.LocalAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filters each HTTP request and looks for a JWT authentication token.
 *
 * If a token is present, it parses the token and sets the security context authentication
 * accordingly. This way, the next HTTP handlers down the chain will have a valid authentication
 * handy.
 *
 */
public class JwtAuthenticationFilter extends GenericFilterBean {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LocalAuthService localAuthService;

    /**
     * Creates a new JWT Filter with the given authentication manager and token converter
     */
    public JwtAuthenticationFilter(LocalAuthService localAuthService) {
        this.localAuthService = localAuthService;
    }


    @Override
    public final void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String stringToken = extractAuthToken(req);

        logger.trace("HTTP Request '"+((HttpServletRequest) request).getRequestURI()+"' with token: " + stringToken);

        // Check if we have an Authorization header

        if (stringToken != null && !stringToken.isEmpty()) {
            try {
                localAuthService.authenticateLocal(stringToken);
                // JWT was valid and this request is now authenticated!
            } catch (AuthenticationException e) {
                logger.error("Authentication failed!", e);
            }
        }else{
            logger.debug("HTTP request without JWT Authorization!");
        }

        chain.doFilter(request, response);
    }



    /**
     * Extract the auth token from the request.
     * Support Authorization headers and ?jwt parameter
     * @param request The servlet request from which a token should be extracted
     * @return Returns the found token or null if no token could be found.
     */
    protected String extractAuthToken(HttpServletRequest request){

        String stringToken = request.getHeader("Authorization");
        if(stringToken == null || stringToken.isEmpty()){
            // No Authorization Header was found. Maybe a simple url parameter was used
            stringToken = request.getParameter("jwt");
        }

        if(stringToken != null){
            // Clean the JWT token
            // Remove the prefixes such as 'Bearer'

            String[] parts = stringToken.split(" ");
            stringToken = parts[parts.length-1].trim(); // Just interpret the last part as JWT token
        }

        return stringToken;
    }

}
