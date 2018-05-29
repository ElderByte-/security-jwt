package com.elderbyte.security.spring.rsa;

import com.elderbyte.security.spring.ElderSpringSecurityJwtSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;


public class PropertyRSAPublicKeyProvider implements RSAPublicKeyProvider{

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ElderSpringSecurityJwtSettings clientSettings;

    public PropertyRSAPublicKeyProvider(ElderSpringSecurityJwtSettings clientSettings){
        this.clientSettings = clientSettings;
    }

    @Override
    public RSAPublicKey getPublicRSAKey(String realm) throws RSAKeyLoadingException {
        return loadRSAKeyFromProperties()
                .orElseThrow(() -> new RSAKeyLoadingException("No RSA key is configured in properties: elder.security.jwt.publicKeyValue"));
    }

    protected Optional<RSAPublicKey> loadRSAKeyFromProperties() throws RSAKeyLoadingException {
        return Optional.ofNullable(clientSettings.getPublicKeyValue())
                .map(rawKey -> loadPublicKey(rawKey));
    }

    protected RSAPublicKey loadPublicKey(String rawKey) throws RSAKeyLoadingException{
        try {
            return RSAKeyPairUtil.loadRSAPublicKey(rawKey);
        } catch (GeneralSecurityException e) {
            throw new RSAKeyLoadingException("Failed to load public key, invalid data.", e);
        }
    }

}
