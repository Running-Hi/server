package com.fourpeople.runninghi.oauth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourpeople.runninghi.common.utils.ApiUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class CustomAccessDeniedHandlerTest {

    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @BeforeEach
    void setUp() {
        customAccessDeniedHandler = new CustomAccessDeniedHandler(new ObjectMapper());
    }

    @Test
    void handle() throws ServletException, IOException {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        AccessDeniedException accessDeniedException = new AccessDeniedException("");

        ObjectMapper objectMapper = new ObjectMapper();

        customAccessDeniedHandler.handle(mockHttpServletRequest,mockHttpServletResponse,accessDeniedException);

        ApiUtils.ApiResult apiResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(), ApiUtils.ApiResult.class);

        assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
        assertThat(mockHttpServletResponse.getCharacterEncoding()).isEqualTo("utf-8");

        assertThat(apiResult.isSuccess()).isFalse();
        assertThat(apiResult.getResponse()).isNull();
        assertThat(apiResult.getError()).isNotNull();
    }
}