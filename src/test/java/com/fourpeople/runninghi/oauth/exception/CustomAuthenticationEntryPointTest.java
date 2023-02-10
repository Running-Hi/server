package com.fourpeople.runninghi.oauth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourpeople.runninghi.common.utils.ApiUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class CustomAuthenticationEntryPointTest {

    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @BeforeEach
    void setUp() {
        customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint(new ObjectMapper());
    }

    @Test
    void commence() throws ServletException, IOException {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        AuthenticationException ex = new AuthenticationCredentialsNotFoundException("");
        ObjectMapper objectMapper = new ObjectMapper();

        customAuthenticationEntryPoint.commence(mockHttpServletRequest,mockHttpServletResponse,ex);

        ApiUtils.ApiResult apiResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(), ApiUtils.ApiResult.class);

        assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(mockHttpServletResponse.getCharacterEncoding()).isEqualTo("utf-8");

        assertThat(apiResult.isSuccess()).isFalse();
        assertThat(apiResult.getResponse()).isNull();
        assertThat(apiResult.getError()).isNotNull();
    }
}