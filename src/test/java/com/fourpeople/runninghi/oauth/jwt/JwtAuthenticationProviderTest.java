package com.fourpeople.runninghi.oauth.jwt;

import com.fourpeople.runninghi.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.DialectOverride;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtAuthenticationProviderTest {
    @Test
    @DisplayName("check if CustomJwtProvider can support jwtAut")
    void supports() {
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(new UserService());
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(null, null);

        Assertions.assertThat(
                        jwtAuthenticationProvider.supports(jwtAuthenticationToken.getClass()))
                .isTrue();
    }
}