package com.elderbyte.security.demo;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class TestResource {

    @GetMapping
    public Mono<String> hello(){
        return Mono.just("Hello world!");
    }


    @Secured("USER")
    @GetMapping("/protected")
    public Mono<String> getProtected(){
        return Mono.just("This is protected!");
    }

}
