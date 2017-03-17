package com.elderbyte.warden.spring.rsa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * Provides the ability to generate, serialize and load RSA key pairs
 */
public class RSAKeyPairUtil {

    private static final Logger logger = LoggerFactory.getLogger(RSAKeyPairUtil.class);


    public static KeyPair generateRSAKeyPair(){

        KeyPair kp = null;

        // RSA signatures require a public and private RSA key pair,
        // the public key must be made known to the JWS recipient in
        // order to verify the signatures
        KeyPairGenerator keyGenerator = null;
        try {
            keyGenerator = KeyPairGenerator.getInstance("RSA");
            keyGenerator.initialize(1024);
            kp = keyGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Failed to generate keypair", e);
        }

        return kp;
    }



    public static RSAPrivateKey loadRSAPrivateKey(String key64) throws GeneralSecurityException {
        byte[] clear = base64Decode(key64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        RSAPrivateKey priv = (RSAPrivateKey)fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return priv;
    }


    public static RSAPublicKey loadRSAPublicKey(String stored) throws GeneralSecurityException {
        byte[] data = base64Decode(stored);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return (RSAPublicKey)fact.generatePublic(spec);
    }

    public static String serializeRSAPrivateKey(RSAPrivateKey priv) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec spec = fact.getKeySpec(priv,
            PKCS8EncodedKeySpec.class);
        byte[] packed = spec.getEncoded();
        String key64 = base64Encode(packed);

        Arrays.fill(packed, (byte) 0);
        return key64;
    }


    public static String serializeRSAPublicKey(RSAPublicKey publ) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = fact.getKeySpec(publ,
            X509EncodedKeySpec.class);
        return base64Encode(spec.getEncoded());
    }


    private static byte[] base64Decode(String encoded){
        return Base64.getDecoder().decode(encoded);

    }

    private static String base64Encode(byte[]  data){
        return Base64.getEncoder().encodeToString(data);
    }

}
