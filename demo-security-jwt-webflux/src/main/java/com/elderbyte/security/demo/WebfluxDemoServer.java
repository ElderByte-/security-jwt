package com.elderbyte.security.demo;

import com.elderbyte.security.rsa.RSAKeyPairUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@SpringBootApplication
public class WebfluxDemoServer {

    private static final Logger log = LoggerFactory.getLogger(WebfluxDemoServer.class);

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication app = new SpringApplication(WebfluxDemoServer.class);

        Environment env = app.run(args).getEnvironment();

        try {
            var keypair = RSAKeyPairUtil.generateRSAKeyPair();
            var mockPrivate = RSAKeyPairUtil.serializeRSAPrivateKey((RSAPrivateKey)keypair.getPrivate());
            var mockPublic = RSAKeyPairUtil.serializeRSAPublicKey((RSAPublicKey)keypair.getPublic());

            log.info("Private: " + mockPrivate);
            log.info("Public: " + mockPublic);

        }catch (Exception e){
            throw new RuntimeException("", e);
        }





        log.info("Access URLs:" +
                        "\n----------------------------------------------------------\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "\n----------------------------------------------------------",
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port")
        );
    }
}
