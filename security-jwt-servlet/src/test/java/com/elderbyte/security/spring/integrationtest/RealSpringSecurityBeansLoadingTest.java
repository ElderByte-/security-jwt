package com.elderbyte.security.spring.integrationtest;

import com.elderbyte.security.ElderSecurityJwtSettings;
import com.elderbyte.security.spring.local.auth.LocalAuthService;
import com.elderbyte.security.spring.local.jwt.JwtAuthenticationFilter;
import com.elderbyte.security.spring.mock.MockAuthenticationServletFilter;
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
        "elder.security.jwt.enableMock=false"
})
public class RealSpringSecurityBeansLoadingTest {

    @Autowired
    private LocalAuthService authService;

    @Autowired
    private ElderSecurityJwtSettings settings;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void contextLoads(){
        Assert.assertTrue(authService != null);
    }


    @Test
    public void ensureMockNotEnabled(){
        Assert.assertFalse("Mock should be disabled by default!", settings.isEnableMock());
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void ensureMockAuthenticationFilterIsNotPresent(){
        applicationContext.getBean(MockAuthenticationServletFilter.class); // Expect bean to be missing

    }

    @Test
    public void ensureStandardJwtFilterIsPresent(){
        Assert.assertNotNull("JwtAuthenticationFilter must be present when mock is enabled!",
                applicationContext.getBean(JwtAuthenticationFilter.class));
    }
}
