package com.elderbyte.warden.spring.local.config;

import com.elderbyte.warden.spring.local.auth.AuthenticationDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Maps AccessDeniedException to a UNAUTHORIZED HTTP CODE
 */
@Order(100) // Give this controller advice a quite high order so it applies before any generic handlers
@ControllerAdvice
public class AccessDeniedExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Map AccessDeniedException to UNAUTHORIZED HTTP CODE
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public void handleAccessDenied(
            @AuthenticationPrincipal AuthenticationDetail currentUser,
            HttpServletRequest req,
            HttpServletResponse reponse,
            Exception exception) throws IOException {


        logger.warn("AccessDeniedException: " + exception.getMessage() +" resource: " +  req.getRequestURL() + " as user: " + currentUser);

        reponse.addHeader("X-Unauthorized", "1");
        reponse.sendError(HttpStatus.UNAUTHORIZED.value(), "You are not allowed to access this api. Reason: " + exception.getMessage());
    }

}
