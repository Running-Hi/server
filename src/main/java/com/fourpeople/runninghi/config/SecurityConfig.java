package com.fourpeople.runninghi.config;

import com.fourpeople.runninghi.oauth.exception.JWTAccessDeniedHandler;
import com.fourpeople.runninghi.oauth.exception.OAuthAuthenticationEntryPoint;
import com.fourpeople.runninghi.oauth.filter.OAuthAccessTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsUtils;

import java.security.PublicKey;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final OAuthAccessTokenAuthenticationFilter oAuthAccessTokenAuthenticationFilter;
    private final OAuthAuthenticationEntryPoint oAuthAuthenticationEntryPoint;
    private final JWTAccessDeniedHandler jwtAccessDeniedHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(oAuthAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(new AntPathRequestMatcher("/oauth/signin/kakao",
                        HttpMethod.POST.name())).permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(oAuthAccessTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // ToDo: add JWTTokenAuthenticationFilter before UsernamePasswordAuthenticationFilter

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}