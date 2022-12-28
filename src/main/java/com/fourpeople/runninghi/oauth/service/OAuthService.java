package com.fourpeople.runninghi.oauth.service;

import com.fourpeople.runninghi.common.utils.ApiUtils;
import com.fourpeople.runninghi.common.utils.ApiUtils.ApiResult;
import com.fourpeople.runninghi.config.auth.JWTManager;
import com.fourpeople.runninghi.config.auth.JWTProperties;
import com.fourpeople.runninghi.config.auth.PrincipalDetails;
import com.fourpeople.runninghi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.fourpeople.runninghi.common.utils.ApiUtils.success;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final UserRepository userRepository;
    public ApiResult<?> signin(String email) {
        return success(userRepository.findByEmail(email)
                .map((user) -> JWTManager.makeUserBothToken(new PrincipalDetails(user)))
                .orElse(JWTManager.makeGuestBothToken()));
    }
}
