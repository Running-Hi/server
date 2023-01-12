package com.fourpeople.runninghi.oauth.service;

import com.fourpeople.runninghi.common.utils.ApiUtils;
import com.fourpeople.runninghi.config.auth.JWTManager;
import com.fourpeople.runninghi.config.auth.PrincipalDetails;
import com.fourpeople.runninghi.oauth.dto.SigninResponseDto;
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

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

    @InjectMocks
    OAuthService oAuthService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("성공: User role jwt 발급")
    void SUCCESS_SIGN_IN_USER() {
        //given
        User user = new User(Role.USER,"id","kakao");
        String email = "DummyEmail@naver.com";
        SigninResponseDto target = JWTManager.makeUserBothToken(new PrincipalDetails(user));

        doReturn(Optional.of(user))
                .when(userRepository)
                .findByProviderId(anyString());

        //when
        ApiUtils.ApiResult<?> signin = oAuthService.signin(email);

        //then
        Assertions.assertThat(signin.isSuccess()).isTrue();
        Assertions.assertThat(signin.getError()).isNull();
        Assertions.assertThat(signin.getResponse()).isNotNull();

        verify(userRepository, times(1))
                .findByProviderId(anyString());
    }

    @Test
    @DisplayName("성공: Guest role jwt 발급")
    void SUCCESS_SIGN_IN_GUEST() {
        //given
        User user = new User(Role.GUEST,"id","kakao");
        String email = "DummyEmail@naver.com";

        SigninResponseDto target = JWTManager.makeGuestBothToken();

        doReturn(Optional.empty())
                .when(userRepository)
                .findByProviderId(email);

        //when
        ApiUtils.ApiResult<?> signin = oAuthService.signin(email);

        //then
        Assertions.assertThat(signin.isSuccess()).isTrue();
        Assertions.assertThat(signin.getError()).isNull();
        Assertions.assertThat(signin.getResponse()).isNotNull();
        verify(userRepository, times(1))
                .findByProviderId(anyString());
    }
}