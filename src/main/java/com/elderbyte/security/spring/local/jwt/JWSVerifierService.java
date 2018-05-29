package com.elderbyte.security.spring.local.jwt;

import com.elderbyte.security.spring.local.config.SecurityConfigurationException;
import com.elderbyte.security.spring.ElderSpringSecurityJwtSettings;
import com.elderbyte.security.spring.rsa.RSAKeyLoadingException;
import com.elderbyte.security.spring.rsa.RSAPublicKeyProvider;
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
import java.util.List;
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


    private final ElderSpringSecurityJwtSettings clientSettings;
    private final RSAPublicKeyProvider rsaPublicKeyProvider;

    private final LoadingCache<String, JWSVerifier> verifierCache;


    /**
     * Creates a new JWSVerifierService
     */
    public JWSVerifierService(ElderSpringSecurityJwtSettings clientSettings, RSAPublicKeyProvider rsaPublicKeyProvider){
        this.clientSettings = clientSettings;
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
     * @throws AuthenticationException Thrown when the given token is not valid / key is wrong
     */
    public void verifyOrThrow(SignedJWT token) throws AuthenticationException {
        String realm = getRealm(token);
        if(realm == null) throw new JwtAuthenticationException("Jwt does not contain a realm/audience claim, can't authenticate.");
        try {
            JWSVerifier verifier = getTokenVerifier(realm);
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
     * Returns the realm from the given jwt token.
     * @param token The jwt
     * @return Returns the realm claim (audience)
     */
    private String getRealm(SignedJWT token){
        try {
            List<String> audience =  token.getJWTClaimsSet().getAudience();
            if(audience.size() > 0){
                return audience.get(0);
            }else{
                throw new JwtAuthenticationException("There was no audience claim in the given JWT - cant deduce realm!");
            }
        } catch (Exception e) {
            throw new JwtAuthenticationException("Failed to extract realm from jwt!", e);
        }
    }

    /**
     * Returns a verifier which can check JSON Web signatures.
     * @throws GeneralSecurityException If the public key could not be found or loaded.
     */
    private JWSVerifier getTokenVerifier(String realm) throws GeneralSecurityException {

        try{
            clientSettings.getRealm().ifPresent(requiredRealm -> {
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
