package com.elderbyte.security.demo;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestResource {

    @GetMapping
    public String hello(){
        return "Hello world (mvc)!";
    }


    @Secured("USER")
    @GetMapping("/protected")
    public String getProtected(){
        return "This is protected (MVC)!";
    }

}
