package com.elderbyte.security.spring.rsa;

import java.security.interfaces.RSAPublicKey;

/**
 * Responsible for loading the public RSA key which is used to authenticate
 * a incoming JWT.
 */
public interface RSAPublicKeyProvider {

    /**
     * Returns the public RSA key for the given realm.
     * (The realm may be ignored in some implementations)
     *
     * @param realm The realm for which the JWT should be loaded.
     * @throws RSAKeyLoadingException Thrown when there was a problem loading an RSA key.
     */
    RSAPublicKey getPublicRSAKey(String realm) throws RSAKeyLoadingException;
}
