package com.elderbyte.security.spring.integrationtest;

import com.elderbyte.security.spring.ElderSpringSecurityJwtSettings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestApp.class)
@TestPropertySource(properties = {
        "warden.client.realm=myRealm"
})
public class ConfigFallBackTest {

    @Autowired
    private ElderSpringSecurityJwtSettings settings;

    @Test
    public void ensureRealmIsSet(){
        Assert.assertEquals("myRealm", settings.getRealm().get());
    }
}
