package com.fourpeople.runninghi.oauth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourpeople.runninghi.common.utils.ApiUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.fourpeople.runninghi.common.utils.ApiUtils.error;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        authException.printStackTrace();
        log.info("Responding with unauthorized error. Message := {}", authException.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        response.getWriter()
                .write(objectMapper.writeValueAsString(
                        error((String) request.getAttribute("errorMessage"), HttpStatus.UNAUTHORIZED)));
    }
}
