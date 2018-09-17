package com.elderbyte.security.spring.local.jwt;


import com.elderbyte.security.spring.local.auth.LocalAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Filters each HTTP request and looks for a JWT authentication token.
 *
 * If a token is present, it parses the token and sets the security context authentication
 * accordingly. This way, the next HTTP handlers down the chain will have a valid authentication
 * handy.
 *
 */
@Order(10000)
public class JwtAuthenticationReactiveWebFilter implements WebFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final LocalAuthService localAuthService;

    @Autowired
    public JwtAuthenticationReactiveWebFilter(LocalAuthService localAuthService){
        logger.info("Setting up webflux security.");
        this.localAuthService = localAuthService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange,
                             WebFilterChain webFilterChain) {

        var request = serverWebExchange.getRequest();
        String stringToken = extractAuthToken(request);

        logger.info("Webflux HTTP Request {} with token: {}", request.getURI(), stringToken);

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

        return webFilterChain.filter(serverWebExchange);
    }


    /**
     * Extract the auth token from the request.
     * Support Authorization headers and ?access_token or ?jwt parameter
     * @param request The servlet request from which a token should be extracted
     * @return Returns the found token or null if no token could be found.
     */
    protected String extractAuthToken(ServerHttpRequest request){

        String jwtToken = null;

        // Get the auth header with the bearer token
        String bearerToken = request.getHeaders().getFirst("Authorization");

        if(bearerToken != null && !bearerToken.isEmpty()){
            // Clean the bearer token
            // Remove the prefixes such as 'Bearer'
            String[] parts = bearerToken.split(" ");
            jwtToken = parts[parts.length-1].trim(); // Just interpret the last part as JWT token

        }

        if(jwtToken == null){
            // No jwt token was extracted from the header

            // Fall back to query params: access_token or jwt:
            jwtToken = request.getQueryParams().getFirst("access_token");

            if(jwtToken == null || jwtToken.isEmpty()) {
                jwtToken = request.getQueryParams().getFirst("jwt");
            }
        }

        return jwtToken;
    }


}
