package com.elderbyte.warden.spring.rsa;


/**
 * Thrown when there was a problem loading an RSA key.
 */
public class RSAKeyLoadingException extends RuntimeException {

    public RSAKeyLoadingException(String message){
        super(message);
    }


    public RSAKeyLoadingException(String message, Exception cause){
        super(message, cause);
    }


}
