package com.elderbyte.warden.spring.local.config;

/**
 * Thrown when the security configuration was not valid
 */
public class SecurityConfigurationException extends RuntimeException {
    public SecurityConfigurationException(String message){
        super(message);
    }
    public SecurityConfigurationException(String message, Throwable cause){
        super(message, cause);
    }
}
