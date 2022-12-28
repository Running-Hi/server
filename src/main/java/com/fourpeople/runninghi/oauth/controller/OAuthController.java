package com.fourpeople.runninghi.oauth.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/oauth")
public class OAuthController {
    @PostMapping("/signin")
    public String signin() {
        return "signin success";
    }

    @GetMapping("/test")
    public String test() {
        return "test success";
    }
}
