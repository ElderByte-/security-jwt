package com.elderbyte.warden.spring.integrationtest;

import com.elderbyte.warden.spring.WardenSpringSecurityJwtSettings;
import com.elderbyte.warden.spring.local.auth.SecurityUtils;
import com.elderbyte.warden.spring.mock.MockJwtHolder;
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
        "warden.client.enableMock=true"
})
public class TestMockUser {

    @Autowired
    private WardenSpringSecurityJwtSettings settings;

    @Autowired
    private MockJwtHolder mockJwtHolder;

    @Test
    public void ensureMockEnabled(){
        Assert.assertTrue("Mocking should be enabled in this test!", settings.isEnableMock());
    }

    @Test
    public void ensureMockLoaded(){
        Assert.assertTrue("There should be a mocked authentication present!", SecurityUtils.getAuthentication() != null);
    }
}
