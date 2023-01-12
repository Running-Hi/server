package com.fourpeople.runninghi.oauth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourpeople.runninghi.common.utils.ApiUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.DataInput;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class OAuthAuthenticationEntryPointTest {

    private OAuthAuthenticationEntryPoint oAuthAuthenticationEntryPoint;

    @BeforeEach
    void setUp() {
        oAuthAuthenticationEntryPoint = new OAuthAuthenticationEntryPoint(new ObjectMapper());
    }

    @Test
    void commence() throws ServletException, IOException {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        AuthenticationException ex = new AuthenticationCredentialsNotFoundException("");
        ObjectMapper objectMapper = new ObjectMapper();

        oAuthAuthenticationEntryPoint.commence(mockHttpServletRequest,mockHttpServletResponse,ex);

        ApiUtils.ApiResult apiResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(), ApiUtils.ApiResult.class);

        assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(mockHttpServletResponse.getCharacterEncoding()).isEqualTo("utf-8");

        assertThat(apiResult.isSuccess()).isFalse();
        assertThat(apiResult.getResponse()).isNull();
        assertThat(apiResult.getError()).isNotNull();
    }
}