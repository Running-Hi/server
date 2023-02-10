package com.fourpeople.runninghi.oauth.filter;

import com.fourpeople.runninghi.oauth.entity.Role;
import com.fourpeople.runninghi.oauth.jwt.Jwt;
import com.fourpeople.runninghi.oauth.jwt.JwtAuthentication;
import com.fourpeople.runninghi.oauth.jwt.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    private static final String UNSUITABLE_ROLE = "unsuitable role";
    private final String headerKey;
    private final Jwt jwt;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String authorizationToken = obtainAuthorizationToken(request);
            if (authorizationToken != null) {
                try {
                    // String to jwt instance
                    Jwt.Claims claims = jwt.verify(authorizationToken);
                    log.debug("Jwt parse result: {}", claims);

                    Long userId = claims.getId();
                    GrantedAuthority authority = new SimpleGrantedAuthority(claims.getRole());

                    if (nonNull(userId) && Role.isOneOfRole(authority.getAuthority())) {
                        JwtAuthenticationToken authentication =
                                new JwtAuthenticationToken(new JwtAuthentication(userId), null, Arrays.asList(authority));

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    log.warn("Jwt processing failed: {}", e.getMessage());
                }
            }
        } else {
            log.debug("SecurityContextHolder not populated with security token, as it already contained: '{}'",
                    SecurityContextHolder.getContext().getAuthentication());
        }

        filterChain.doFilter(request, response);
    }

    private String obtainAuthorizationToken(HttpServletRequest request) throws UnsupportedEncodingException {
        return Optional.ofNullable(request.getHeader(headerKey))
                .filter(token -> toke)
                .orElse(null);
        if (token != null) {
            if (log.isDebugEnabled())
                log.debug("Jwt authorization api detected: {}", token);
            try {

                token = URLDecoder.decode(token, "UTF-8");
                String[] parts = token.split(" ");
                if (parts.length == 2) {
                    String scheme = parts[0];
                    String credentials = parts[1];
                    return BEARER.matcher(scheme).matches() ? credentials : null;
                }
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }

        return null;
    }
}
