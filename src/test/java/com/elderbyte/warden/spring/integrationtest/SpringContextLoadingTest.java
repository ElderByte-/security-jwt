package com.elderbyte.warden.spring.integrationtest;

import com.elderbyte.warden.spring.local.auth.LocalAuthService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestApp.class)
public class SpringContextLoadingTest {

    @Autowired
    private LocalAuthService authService;

    @Test
    public void contextLoads(){
        Assert.assertTrue(authService != null);
    }
}
