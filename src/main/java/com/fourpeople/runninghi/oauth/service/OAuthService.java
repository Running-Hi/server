package com.fourpeople.runninghi.oauth.service;

import com.fourpeople.runninghi.common.utils.ApiUtils.ApiResult;
import com.fourpeople.runninghi.config.auth.JWTManager;
import com.fourpeople.runninghi.config.auth.PrincipalDetails;
import com.fourpeople.runninghi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.fourpeople.runninghi.common.utils.ApiUtils.success;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final UserRepository userRepository;
    public ApiResult<?> signin(String providerId) {
        return success(userRepository.findByProviderId(providerId)
                .map((user) -> JWTManager.makeUserBothToken(new PrincipalDetails(user)))
                .orElse(JWTManager.makeGuestBothToken()));
    }
}
