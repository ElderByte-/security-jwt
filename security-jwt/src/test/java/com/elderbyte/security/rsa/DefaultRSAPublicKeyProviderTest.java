package com.elderbyte.security.rsa;

import com.elderbyte.security.spring.settings.ElderSecurityJwtSettingsFallback;
import com.elderbyte.security.spring.settings.ElderSpringSecurityJwtProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultRSAPublicKeyProviderTest {

    private DefaultRSAPublicKeyProvider rsaPublicKeyProviderEmpty;
    private DefaultRSAPublicKeyProvider rsaPublicKeyProviderUrl;
    private DefaultRSAPublicKeyProvider rsaPublicKeyProviderValue;

    private String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnklC8EwRnfkv9jqMeAYiJbhCKo3B1I/" +
            "GoPrLoxDmRIiCY+kkDuKdKcCwVqiOb1v8LtD25wP9QeeG4S2DA2gOUyP/fUxdb5W/6dlgXS3Cx" +
            "8QjE2Nt9VfTnqiVkJQZgPG/VA1Lh/Oodc4vIr8/GWVKjQ60IgzgmtY8ljcVmlTgmYJ4fHSAVQp" +
            "wsoiN/kTIZX6lrWnEFp6II3jRpJ+Az6PWPP5E33E8yDWkYTdhmQFpa8DnJci0txjdz2xN/Ukul" +
            "TY8G4X1PbYvkPqqGCnDd24xMWl6ioqRb7MFuOLTC16/e8EEm04CutOobxk+pFwpkHxaQldsXgv" +
            "HFz5RGhVrcBJvCQIDAQAB";

    @Before
    public void setup(){
        var properties = new ElderSpringSecurityJwtProperties();
        properties.getPublicKey().setUrl("http://myserver/realms/{realm}/key");

        var adapter = new ElderSecurityJwtSettingsFallback(properties);
        rsaPublicKeyProviderUrl = new DefaultRSAPublicKeyProvider(adapter);

        rsaPublicKeyProviderEmpty = new DefaultRSAPublicKeyProvider(new ElderSecurityJwtSettingsFallback(new ElderSpringSecurityJwtProperties()));


        var value = new ElderSpringSecurityJwtProperties();
        value.getPublicKey().setValue(pubKey);
        rsaPublicKeyProviderValue = new DefaultRSAPublicKeyProvider(new ElderSecurityJwtSettingsFallback(value));
    }

    @Test
    public void getPublicRSAKey_Value() throws Exception {
        var key = rsaPublicKeyProviderValue.getPublicRSAKey("test");
        Assert.assertEquals(pubKey,  RSAKeyPairUtil.serializeRSAPublicKey(key));
    }

    @Test(expected = RSAKeyLoadingException.class)
    public void getPublicRSAKey_Empty() {
        var key = rsaPublicKeyProviderEmpty.getPublicRSAKey("test");
    }

    @Test
    public void obtainUrl() {
        var url = rsaPublicKeyProviderUrl.obtainUrl("test");
        Assert.assertEquals("http://myserver/realms/test/key",  url);
    }

}