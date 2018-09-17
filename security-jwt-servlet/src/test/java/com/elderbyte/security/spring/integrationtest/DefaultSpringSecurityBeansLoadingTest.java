package com.elderbyte.security.spring.integrationtest;

import com.elderbyte.security.ElderSecurityJwtSettings;
import com.elderbyte.security.spring.settings.ElderSecurityJwtSettingsFallback;
import com.elderbyte.security.spring.local.auth.LocalAuthService;
import com.elderbyte.security.spring.local.auth.SecurityUtils;
import com.elderbyte.security.spring.local.jwt.JwtAuthenticationFilter;
import com.elderbyte.security.spring.mock.MockAuthenticationFilter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestApp.class)
public class DefaultSpringSecurityBeansLoadingTest {

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

    @Test
    public void ensureMockNotLoaded(){
        Assert.assertNull("There should be no authentication context when mock is disabled!",
                SecurityUtils.getAuthentication());
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void ensureMockAuthenticationFilterIsNotPresent(){
        applicationContext.getBean(MockAuthenticationFilter.class); // Expect bean to be missing

    }

    @Test
    public void ensureStandardJwtFilterIsPresent(){
        Assert.assertNotNull("JwtAuthenticationFilter must be present when mock is enabled!",
                applicationContext.getBean(JwtAuthenticationFilter.class));
    }

}
