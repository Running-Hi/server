package com.fourpeople.runninghi.oauth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fourpeople.runninghi.oauth.entity.KakaoAccount;
import com.fourpeople.runninghi.oauth.entity.KakaoUserInfo;
import com.fourpeople.runninghi.oauth.entity.OAuthAccessToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthAccessTokenAuthenticationFilter extends OncePerRequestFilter {
    private final RestTemplate restTemplate;
    private final String REQUEST_RESOURCE_SERVER_URL = "https://kapi.kakao.com/v2/user/me";
    private final String URI = "/oauth/signin/kakao";
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        log.info("Enter the OAuthAccessTokenAuthenticationFilter");
        try {
            OAuthAccessToken oAuthAccessToken = objectMapper.readValue(request.getInputStream(), OAuthAccessToken.class);
            log.info("OAuthAccessToken: {}", oAuthAccessToken);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", oAuthAccessToken.getAuthorizationValue());

            ResponseEntity<KakaoUserInfo> exchange = restTemplate.exchange(REQUEST_RESOURCE_SERVER_URL, HttpMethod.GET, new HttpEntity<>(httpHeaders), KakaoUserInfo.class);
            KakaoUserInfo body = exchange.getBody();

            log.info("KakaoAccount: {}", body);
            SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
            emptyContext.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(body.getKakaoAccount().getEmail(), null, null));
            SecurityContextHolder.setContext(emptyContext);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage",e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().equals(URI);
    }
}
