package com.fourpeople.runninghi.oauth.controller;

import com.fourpeople.runninghi.common.utils.ApiUtils.ApiResult;
import com.fourpeople.runninghi.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @PostMapping("/signin/kakao")
    public ResponseEntity<ApiResult<?>> signin(@AuthenticationPrincipal String providerId) {
        log.info("providerId: {}", providerId);

        return new ResponseEntity<>(oAuthService.signin(providerId), HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "test success";
    }
}
