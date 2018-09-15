package com.elderbyte.security.spring.integrationtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

@RestController
@Secured("ROLE_USER")
@RequestMapping("/api")
@SpringBootApplication
public class IntegrationTestApp {

    private static final Logger log = LoggerFactory.getLogger(IntegrationTestApp.class);

    /**
     * Starts the standalone Warder Server
     */
    public static void main(String[] args) throws UnknownHostException {

        SpringApplication app = new SpringApplication(IntegrationTestApp.class);
        Environment env = app.run(args).getEnvironment();
    }


    @GetMapping
    public String test(){
        return "foo";
    }
}