package com.fourpeople.runninghi.oauth.filter;

import com.auth0.jwt.JWT;
import com.fourpeople.runninghi.config.auth.JWTManager;
import com.fourpeople.runninghi.config.auth.JWTProperties;
import com.fourpeople.runninghi.config.auth.PrincipalDetails;
import com.fourpeople.runninghi.user.entity.User;
import com.fourpeople.runninghi.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

// /login 아니면 거의 모두 거치는 필터
@Slf4j
public class JwtVerificationFilter extends BasicAuthenticationFilter {
    private final JWTManager jwtManager;
    private final UserRepository userRepository;
    private final JWTProperties jwtProperties;

    public JwtVerificationFilter(AuthenticationManager authenticationManager, JWTManager jwtManager, UserRepository userRepository,JWTProperties jwtProperties) {
        super(authenticationManager);
        this.jwtManager = jwtManager;
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("filtered BasicAuthenticationFilter");
        // npe 방지
        String jwtHeader = request.getHeader(jwtProperties.HEADER_KEY);
        if (jwtHeader == null) {
            chain.doFilter(request, response);
            return;
        }

        String token = jwtHeader.replace(jwtProperties.PREFIX, "");
        if (JWTManager.isValidJWT(token)) {
            // Authenticaion 생성해서 세션에 넣어줌

            Long userId = Long.parseLong(JWT.decode(token).getClaim("id").toString());

            log.info("userId = " + userId);

            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not valid user id"));
            PrincipalDetails principalDetails = new PrincipalDetails(user);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        chain.doFilter(request, response);
    }
}