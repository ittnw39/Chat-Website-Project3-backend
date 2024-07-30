package com.elice.spatz.domain.user.controller;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/password")
    public String getPassword() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        String rawPassword = "1234";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        return encodedPassword;
    }
}
