package com.elderbyte.warden.spring.local.jwt;

import com.elderbyte.warden.spring.local.config.SecurityConfigurationException;
import com.elderbyte.warden.spring.WardenSpringSecurityJwtSettings;
import com.elderbyte.warden.spring.rsa.RSAKeyLoadingException;
import com.elderbyte.warden.spring.local.auth.AuthenticationDetail;
import com.elderbyte.warden.spring.rsa.RSAPublicKeyProvider;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;

/**
 * Provides the ability to validate a incoming JWT.
 * The validation includes the RSA key check, i.e. this service uses the public RSA key
 * to validate if the token has been signed by the corresponding private key.
 *
 * The public key can either be configured using properties, or the warden client can
 * fetch the public key directly form the warden server.
 *
 */
public class JWSVerifierService {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    private final WardenSpringSecurityJwtSettings wardenClientSettings;
    private final RSAPublicKeyProvider rsaPublicKeyProvider;

    private final LoadingCache<String, JWSVerifier> verifierCache;


    /**
     * Creates a new JWSVerifierService
     */
    public JWSVerifierService(WardenSpringSecurityJwtSettings wardenClientSettings, RSAPublicKeyProvider rsaPublicKeyProvider){
        this.wardenClientSettings = wardenClientSettings;
        this.rsaPublicKeyProvider = rsaPublicKeyProvider;

        verifierCache = Caffeine.newBuilder()
                .maximumSize(100)
                .refreshAfterWrite(5, TimeUnit.MINUTES)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(realm -> buildJWSVerifier(realm));
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Verifies the given JWT token
     * @param token The JWT token
     * @param auth authentication details
     * @throws AuthenticationException Thrown when the given token is not valid / key is wrong
     */
    public void verifyOrThrow(SignedJWT token, AuthenticationDetail auth) throws AuthenticationException {
        try {
            JWSVerifier verifier = getTokenVerifier(auth);

            if (!token.verify(verifier)) {
                throw new JwtAuthenticationException("Authentication failed - MAC/RSA signature not matching!");
            }
        } catch (JOSEException e) {
            throw new JwtAuthenticationException("Authentication failed", e);
        } catch (GeneralSecurityException e) {
            throw new JwtAuthenticationException("Failed to load public RSA Key!", e);
        }
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    /**
     * Returns a verifier which can check JSON Web signatures.
     * @throws GeneralSecurityException If the public key could not be found or loaded.
     */
    private JWSVerifier getTokenVerifier(AuthenticationDetail auth) throws GeneralSecurityException {

        if(auth.getRealm() == null) throw new JwtAuthenticationException("Jwt does not contain a realm/audience claim, can't authenticate.");

        String realm = auth.getRealm();

        try{
            wardenClientSettings.getRealm().ifPresent(requiredRealm -> {
                if(!requiredRealm.equals(realm)){
                    throw new RuntimeException("This client is configured to only accept realm " + requiredRealm + " but the received token was from realm " + realm+"!");
                }
            });
            return verifierCache.get(realm);
        }catch (Exception e){
            throw new GeneralSecurityException("Failed to verify JWT - token will not be accepted!", e);
        }
    }


    private JWSVerifier buildJWSVerifier(String realm) throws SecurityConfigurationException{
        try {
            RSAPublicKey publicKey = rsaPublicKeyProvider.getPublicRSAKey(realm);
            return new RSASSAVerifier(publicKey);
        } catch (RSAKeyLoadingException e) {
            throw new SecurityConfigurationException("Public RSA key could not be loaded!", e);
        }
    }

}
