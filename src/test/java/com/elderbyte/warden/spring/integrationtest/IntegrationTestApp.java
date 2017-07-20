package com.elderbyte.warden.spring.integrationtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;


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
}