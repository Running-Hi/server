package com.fourpeople.runninghi.oauth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CustomBearerTokenAuthenticationFilter extends OncePerRequestFilter {
    private BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        try {
            token = this.bearerTokenResolver.resolve(request);
        } catch (OAuth2AuthenticationException invalid) {
            this.logger.trace("Sending to authentication entry point since failed to resolve bearer token", invalid);
            this.authenticationEntryPoint.commence(request, response, invalid);
            return;
        }

        if (token == null) {
            this.logger.trace("Did not process request since did not find bearer token");
            filterChain.doFilter(request, response);
            return;
        }

        BearerTokenAuthenticationToken authenticationRequest = new BearerTokenAuthenticationToken(token);
        authenticationRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

        try {
            AuthenticationManager authenticationManager = this.authenticationManagerResolver.resolve(request);
            Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
            SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authenticationResult);
            this.securityContextHolderStrategy.setContext(context);
            this.securityContextRepository.saveContext(context, request, response);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authenticationResult));
            }
            filterChain.doFilter(request, response);
        }
        catch (AuthenticationException failed) {
            this.securityContextHolderStrategy.clearContext();
            this.logger.trace("Failed to process authentication request", failed);
            this.authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
        }
    }
}
