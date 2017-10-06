package com.elderbyte.warden.spring.mock;

import com.elderbyte.warden.spring.WardenSpringSecurityJwtSettings;
import com.elderbyte.warden.spring.local.auth.AuthenticationDetail;
import com.elderbyte.warden.spring.local.auth.AuthenticationDetailImpl;
import com.elderbyte.warden.spring.local.auth.AuthorityUtil;
import com.elderbyte.warden.spring.rsa.RSAKeyPairUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



public class MockJwtHolder {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RSAPublicKey mockPublicKey;
    private final RSAPrivateKey mockPrivateKey;
    private final WardenSpringSecurityJwtSettings clientSettings;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/


    @Autowired
    public MockJwtHolder(WardenSpringSecurityJwtSettings clientSettings){

        this.clientSettings = clientSettings;

        try {
            // Load a constant mock RSA key so we can even support inter service communication.

            mockPublicKey = RSAKeyPairUtil.loadRSAPublicKey(
                    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCid7XA8oiQpSeN/AubqVX6KQF0Ui5Li/wZtyHqKW/b7lvI" +
                    "JbNRLDk2kUztjux0XiU7cr+KabxVD3JjnyhEkmaWI3yCMrNPGZztl4xmhuRkgjcfSuk9BmL3KotRwgiK+1yP" +
                    "eSqL3VPjYn24Ucun9t4A2GKX5DpwTs+KIBdw3oT+ewIDAQAB");

            mockPrivateKey = RSAKeyPairUtil.loadRSAPrivateKey(
                    "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKJ3tcDyiJClJ438C5upVfopAXRSLkuL/Bm3I" +
                    "eopb9vuW8gls1EsOTaRTO2O7HReJTtyv4ppvFUPcmOfKESSZpYjfIIys08ZnO2XjGaG5GSCNx9K6T0GYvcqi1" +
                    "HCCIr7XI95KovdU+NifbhRy6f23gDYYpfkOnBOz4ogF3DehP57AgMBAAECgYALxn1n5Kw9P5skUVZOC/HtVbe" +
                    "DRMmSNC0rd/h/Tk2LNTV1xjW+9mOXLuTSev9sV75/FowHbi52Q85Yphp3Dd1deqTp3E6LpGQ/6MebeN3Q5h+6" +
                    "8ZAFZ5sabbGV6ZqPu5IMuxuQ4sKxoKTu1G0kVaacA9gxWrHERI1SEN6BfmlvMQJBAOVGa//nUz0v0IDpooVEp" +
                    "kLgElpvZjoKnKh9RPvZa6Gfg5pdPtJXccuyRXZFOya9T33sg4hAMD2wH6ezg/Bmj30CQQC1Z8Ad3SAHHd9FAu" +
                    "Z5M21olumDxauz2XvFo1wLCTdhZzrN3O5HQ1koEJHvd7mEMWsM13TkDiOzedFmJKIuvhdXAkEAsH6fkOVwbH+" +
                    "sUDCKTXDlVEHNnxR2a2PT6NhWDFH4yvhShLgXLjDmhtn7Iup0eIPu947kSjSvbi1wkNZeUOeCuQJBALFFGMVW" +
                    "MBUkSOmcgvKPoeviBYHiqf7PmZcyIyEZuYeypBFGQ4daNgbj4mh7u8uFq1HaPn0ZoCOj+E/znpVE53MCQQDMX" +
                    "LPfvkeOVYd4kN14hKsa0M3Ca3+xcSCNSlL/lBfSzErDSLbcrw1attnCs6mAENG1frYh6pJOc66u09QT3UZM");

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Failed to load mock RSA Key", e);
        }
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    @PostConstruct
    public void authenticateWithMock(){
        MockUser mockUser;

        if(!clientSettings.getMockUsers().isEmpty()){
            mockUser = clientSettings.getMockUsers().get(0);
        }else{
            mockUser = defaultMockUser();
        }
        authenticateWithMock(mockUser);
    }

    public RSAPublicKey getMockPublicKey() {
        return mockPublicKey;
    }

    public JWT getSignedJWTMock(MockUser user){
        try {
            // Create RSA-signer with the private key
            JWSSigner signer = new RSASSASigner(mockPrivateKey);

            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

            List<String> roles = Arrays.asList(user.getRoles());

            builder.issuer("Warden Client Mocker");
            builder.subject(user.getRealm()+"/"+user.getLogin());
            builder.claim("name", user.getFullName());
            builder.claim("lang", "en");
            builder.claim("roles", roles);
            builder.claim("realm", user.getRealm());


            builder.expirationTime(new Date(new Date().getTime() + (12*60*60*1000)));
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), builder.build());
            signedJWT.sign(signer);

            return signedJWT;
        }catch (JOSEException e){
            throw new RuntimeException("Failed to create mock JWT token!", e);
        }
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private MockUser defaultMockUser(){
        MockUser mockUser = new MockUser();

        mockUser.setLogin("foo.mock");
        mockUser.setRealm("mock");
        mockUser.setFullName("Default Foo Bar");
        mockUser.setRoles(Arrays.asList("USER").toArray(new String[0]));

        return mockUser;
    }

    private void authenticateWithMock(MockUser mockUser){
        AuthenticationDetail auth = new AuthenticationDetailImpl(
                mockUser.getRealm(),
                mockUser.getLogin(),
                mockUser.getFullName(),
                AuthorityUtil.createAuthorities(mockUser.getRoles()),
                getSignedJWTMock(mockUser)
        );
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
        logger.warn("SecurityContextHolder populated with mock authentication. For development purposes only!");
    }


}
