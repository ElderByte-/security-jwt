package com.elderbyte.security.rsa;

import com.elderbyte.security.ElderSecurityJwtSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;


public class DefaultRSAPublicKeyProvider implements RSAPublicKeyProvider{

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ElderSecurityJwtSettings securitySettings;
    private final RestOperations restOperations;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public DefaultRSAPublicKeyProvider(ElderSecurityJwtSettings securitySettings){
        this.securitySettings = securitySettings;
        this.restOperations = new RestTemplate();

    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    @Override
    public RSAPublicKey getPublicRSAKey(String realm) throws RSAKeyLoadingException {
        return loadRSAKeyFromProperties()
                .orElseGet(() -> fetchPublicRSAKeyFromUrl(realm));
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private Optional<RSAPublicKey> loadRSAKeyFromProperties() throws RSAKeyLoadingException {
        return Optional.ofNullable(securitySettings.getPublicKey().getValue())
                .map(this::loadPublicKey);
    }

    private RSAPublicKey loadPublicKey(String rawKey) throws RSAKeyLoadingException{
        try {
            return RSAKeyPairUtil.loadRSAPublicKey(rawKey);
        } catch (GeneralSecurityException e) {
            throw new RSAKeyLoadingException("Failed to load public key, invalid data.", e);
        }
    }

    private RSAPublicKey fetchPublicRSAKeyFromUrl(String realm) throws RSAKeyLoadingException {
        String rawKey = null;


        var obtainUrl = obtainUrl(realm);

        try {
            var response = restOperations.getForEntity(obtainUrl, String.class);
            rawKey = response.getBody();
        } catch (RestClientException e) {
            throw new RSAKeyLoadingException("Loading public RSA key from Warden Server failed - request timed out!", e);
        }

        if(rawKey == null || rawKey.isEmpty()){
            throw new RSAKeyLoadingException("The public RSA key retrieved from the Warden Server was null/empty!");
        }
        return loadPublicKey(rawKey);
    }

    protected String obtainUrl(String realm){
        var obtainUrlTemplate = securitySettings.getPublicKey().getUrl(); // "http://myserver/api/realms/{realm}/pubkey"
        if(obtainUrlTemplate != null && !obtainUrlTemplate.isEmpty()){
            return obtainUrlTemplate.replaceAll("\\{realm\\}", realm);
        }else{
            throw new RSAKeyLoadingException("You must either configure the publicKey.value or the obtain url publicKey.url!");
        }

    }

}
