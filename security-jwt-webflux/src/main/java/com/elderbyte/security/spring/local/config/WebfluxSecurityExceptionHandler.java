package com.elderbyte.security.spring.local.config;


import com.elderbyte.security.spring.local.auth.AuthenticationDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;


@Order(100) // Give this controller advice a quite high order so it applies before any generic handlers
@ControllerAdvice
public class WebfluxSecurityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = { AuthenticationCredentialsNotFoundException.class } )
    public Mono<Void> handleUnauthorized(
            @AuthenticationPrincipal AuthenticationDetail currentUser,
            ServerWebExchange webExchange,
            Exception exception) throws IOException {

        var request = webExchange.getRequest();
        var response = webExchange.getResponse();

        var errorMessage = exception.getClass().getSimpleName() + ": " + exception.getMessage() +" resource: " +  request.getURI() + " as user: " + currentUser;


        logger.warn(errorMessage);

        response.getHeaders().set("X-Unauthorized", "1");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return sendResponse(response, errorMessage);
    }

    @ExceptionHandler(value = { AccessDeniedException.class } )
    public Mono<Void> handleAccessDenied(
            @AuthenticationPrincipal AuthenticationDetail currentUser,
            ServerWebExchange webExchange,
            Exception exception) throws IOException {

        var request = webExchange.getRequest();
        var response = webExchange.getResponse();

        var errorMessage = exception.getClass().getSimpleName() + ": " + exception.getMessage() +" resource: " +  request.getURI() + " as user: " + currentUser;


        logger.warn(errorMessage);

        response.setStatusCode(HttpStatus.FORBIDDEN);

        return sendResponse(response, errorMessage);
    }

    private Mono<Void> sendResponse(ServerHttpResponse response, String errorMessage){
        byte[] bytes = errorMessage.getBytes(StandardCharsets.UTF_8);
        var buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(buffer));
    }

}
