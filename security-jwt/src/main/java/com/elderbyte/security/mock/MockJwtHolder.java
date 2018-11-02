package com.elderbyte.security.mock;

import com.elderbyte.security.ElderSecurityJwtSettings;
import com.elderbyte.security.spring.local.auth.AuthenticationDetail;
import com.elderbyte.security.spring.local.auth.AuthenticationDetailImpl;
import com.elderbyte.security.spring.local.auth.AuthorityUtil;
import com.elderbyte.security.rsa.RSAKeyPairUtil;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



public class MockJwtHolder implements InitializingBean {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RSAPublicKey mockPublicKey;
    private final RSAPrivateKey mockPrivateKey;
    private final ElderSecurityJwtSettings clientSettings;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/


    @Autowired
    public MockJwtHolder(ElderSecurityJwtSettings clientSettings){

        this.clientSettings = clientSettings;

        try {
            // Load a constant mock RSA key so we can even support inter service communication.

            mockPublicKey = RSAKeyPairUtil.loadRSAPublicKey(
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnklC8EwRnfkv9jqMeAYiJbhCKo3B1I/" +
                            "GoPrLoxDmRIiCY+kkDuKdKcCwVqiOb1v8LtD25wP9QeeG4S2DA2gOUyP/fUxdb5W/6dlgXS3Cx" +
                            "8QjE2Nt9VfTnqiVkJQZgPG/VA1Lh/Oodc4vIr8/GWVKjQ60IgzgmtY8ljcVmlTgmYJ4fHSAVQp" +
                            "wsoiN/kTIZX6lrWnEFp6II3jRpJ+Az6PWPP5E33E8yDWkYTdhmQFpa8DnJci0txjdz2xN/Ukul" +
                            "TY8G4X1PbYvkPqqGCnDd24xMWl6ioqRb7MFuOLTC16/e8EEm04CutOobxk+pFwpkHxaQldsXgv" +
                            "HFz5RGhVrcBJvCQIDAQAB");

            mockPrivateKey = RSAKeyPairUtil.loadRSAPrivateKey(
                    "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCeSULwTBGd+S/2Oox4BiIluEI" +
                            "qjcHUj8ag+sujEOZEiIJj6SQO4p0pwLBWqI5vW/wu0PbnA/1B54bhLYMDaA5TI/99TF1vlb/p2" +
                            "WBdLcLHxCMTY231V9OeqJWQlBmA8b9UDUuH86h1zi8ivz8ZZUqNDrQiDOCa1jyWNxWaVOCZgnh" +
                            "8dIBVCnCyiI3+RMhlfqWtacQWnogjeNGkn4DPo9Y8/kTfcTzINaRhN2GZAWlrwOclyLS3GN3Pb" +
                            "E39SS6VNjwbhfU9ti+Q+qoYKcN3bjExaXqKipFvswW44tMLXr97wQSbTgK606hvGT6kXCmQfFp" +
                            "CV2xeC8cXPlEaFWtwEm8JAgMBAAECggEAWoR+2ThHItXrzZ2x0rl/MZ7rh7ZFUC9gYZLKfVget" +
                            "rZyCny6+CAzzDq7HPOLcLdjwcoSSA4J+HRUt9T1BS+ucXE2nArdrAAxHaQH7YFL0D2j6WSE7zb" +
                            "od+Tkkyd+ZjTZO1HXDcoTXjsozXAcv4MjXQM5k/dVQnadczHREs7TGjWSDq8Ce3sjzyCfofjp0" +
                            "1+doqQMpboplA273OiPVxn16vamEE+IDBi2rn3efYFOsSTfRJLn29qUFCrmojmOH/jnEHN9n8i" +
                            "40NlS/A4d+ZJvisUBdT02XaVP6enR8smnIe6m2SpqQZ6Ne4QPjcdIDzEfrdKZUMLPTaIAVAwAN" +
                            "rMZ+QKBgQDR2gdNY1u+d8ZDOuztNhD/ZZNm69PsXt6TjhGySj1473UQgLr9OqpJfulxPIwLtuY" +
                            "Hapcz0UpPh+q+XaWeiXpae1JHuS68SHVxlNtdh5K9Vluu9wsK7T3La+Nei+ZFmvKcaUFUeaL6A" +
                            "TvNytX3j0fl3Q6X4UWTW2jY1YLcSybynwKBgQDBGEMWNf3h2eWkdZs8P0eACm6HZVojq2pqKzv" +
                            "8ueC8YSnXYmA75OrJslXwEJyumxs3UWKSdgNX++lCQ+3SyOVEpcyaOBrrpIdTY5c6BJCmKYVbl" +
                            "6kIA2jagz+4EBQlzx3GLFSZFE6emF5bDBtBYle46hykNmrco71wk4FpkhslVwKBgQCjr0wI2hp" +
                            "iq+xS3nCV6plBjUp3ejV4Ztt3/tXg2rWuUiFeuPqhsRPIdhzCfbbDIGu2lzbPbU1oUZ0R1dvsm" +
                            "FF+qDiax2Aj7uTkrnaZMrmLxAOJqxrK8BhGRqY4RilcW6snw5X4vVQZa8LpF3DkQ7eSHWZXo5h" +
                            "wg1l54LjM+jWsAwKBgQCwvJVC8h4zp24yjlaM2VqHToUGNnU/qrjW/kWEt1mE8uYKNwfgKlHgV" +
                            "POlrH7MrjlF857mcU/0KyXAOk6mnKyEncDc8zdosYoPgFs9hkojXGOshu7237AMOQ+MWGMojB8" +
                            "zcQXpqxWewdWzPiyANxQVs2qNvyUmWFSfgtNznoBsNwKBgD4MB9aXw9sbBJH/g1yPT+6/km1rl" +
                            "Q+/d9WEN+9Sy5LGrH5HZdjW9RqmfhJLT8MVVWlEBpesXtWRB/4wvJAg7zDvohXqg8+6hcH4MRA" +
                            "QP2JgllUyTl5gtOSdfupAOJmIG+qnQWZ5ILXjGwh/pjO2VLRWGwcCPBGOk1DsapyYz50D");

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Failed to load mock RSA Key", e);
        }
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    @Override
    public void afterPropertiesSet() throws Exception {
        authenticateWithMock();
    }

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

            builder.issuer("Elder Security Mocker");
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
        mockUser.setRoles(Arrays.asList("USER", "MOCK").toArray(new String[0]));

        return mockUser;
    }

    private void authenticateWithMock(MockUser mockUser){
        AuthenticationDetail auth = new AuthenticationDetailImpl(
                mockUser.getRealm(),
                mockUser.getLogin(),
                mockUser.getRealm() + "-" + mockUser.getLogin(),
                mockUser.getFullName(),
                AuthorityUtil.createAuthorities(mockUser.getRoles()),
                getSignedJWTMock(mockUser)
        );
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
        logger.warn("SecurityContextHolder populated with mock authentication. For development purposes only!");
    }



}
