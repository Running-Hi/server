package com.fourpeople.runninghi.oauth.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        accessDeniedException.printStackTrace();
        log.info("Responding with insufficient authorization error. Message := {}", accessDeniedException.getMessage());
        response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                accessDeniedException.getLocalizedMessage()
        );
    }
}
