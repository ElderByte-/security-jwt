package com.elderbyte.security.spring.integrationtest;

import com.elderbyte.security.spring.ElderSpringSecurityJwtSettings;
import com.elderbyte.security.spring.local.auth.SecurityUtils;
import com.elderbyte.security.spring.local.jwt.JwtAuthenticationFilter;
import com.elderbyte.security.spring.mock.MockAuthenticationFilter;
import com.elderbyte.security.spring.mock.MockJwtHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestApp.class)
@TestPropertySource(properties = {
        "elder.security.jwt.enableMock=true"
})
public class MockSpringSecurityBeansLoadingTest {

    @Autowired
    private ElderSpringSecurityJwtSettings settings;

    @Autowired
    private MockJwtHolder mockJwtHolder;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void ensureMockEnabled(){
        Assert.assertTrue("Mocking should be enabled in this test!", settings.isEnableMock());
    }

    @Test
    public void ensureMockLoaded(){
        Assert.assertNotNull("There should be a mocked authentication present!",
                SecurityUtils.getAuthentication());
    }

    @Test
    public void ensureMockAuthenticationFilterIsPresent(){
        Assert.assertNotNull("MockAuthenticationFilter must be present when mock is enabled!",
                applicationContext.getBean(MockAuthenticationFilter.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void ensureStandardJwtFilterIsNotPresent(){
        applicationContext.getBean(JwtAuthenticationFilter.class); // Expect bean to be missing
    }

}
