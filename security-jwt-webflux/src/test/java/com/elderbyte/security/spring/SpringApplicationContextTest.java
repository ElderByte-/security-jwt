package com.elderbyte.security.spring;

import com.elderbyte.security.spring.integrationtest.IntegrationTestApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestApp.class)
public class SpringApplicationContextTest {

    @Test
    public void ensureContextLoads(){

    }

}
