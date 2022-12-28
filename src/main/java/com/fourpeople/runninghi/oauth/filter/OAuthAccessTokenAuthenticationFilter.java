package com.fourpeople.runninghi.oauth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourpeople.runninghi.oauth.entity.KakaoAccount;
import com.fourpeople.runninghi.oauth.entity.OAuthAccessToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthAccessTokenAuthenticationFilter extends OncePerRequestFilter {
    private final RestTemplate restTemplate;
    String reqURL = "https://kapi.kakao.com/v2/user/me";
    String URL = "/oauth/login";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (requestURI.equals(URL)) {
            System.out.println("123123");
            ObjectMapper objectMapper = new ObjectMapper();
            OAuthAccessToken oAuthAccessToken = objectMapper.readValue(request.getReader(), OAuthAccessToken.class);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", oAuthAccessToken.getAuthorizationValue());

            KakaoAccount kakaoAccount = restTemplate.exchange(reqURL, HttpMethod.GET, new HttpEntity<>(httpHeaders), KakaoAccount.class).getBody();

            SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();

            emptyContext.setAuthentication(new UsernamePasswordAuthenticationToken(kakaoAccount.getEmail(), null, null));
        }

        filterChain.doFilter(request, response);
    }
}
