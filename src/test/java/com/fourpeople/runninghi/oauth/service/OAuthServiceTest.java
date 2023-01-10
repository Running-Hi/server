package com.fourpeople.runninghi.oauth.service;

import com.fourpeople.runninghi.common.utils.ApiUtils;
import com.fourpeople.runninghi.oauth.entity.Role;
import com.fourpeople.runninghi.user.entity.User;
import com.fourpeople.runninghi.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

    @InjectMocks
    OAuthService oAuthService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("성공: 로그인 성공")
    void SUCCESS_SIGN_IN() {

    }
}