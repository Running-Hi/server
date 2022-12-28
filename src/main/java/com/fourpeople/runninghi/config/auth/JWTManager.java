package com.fourpeople.runninghi.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fourpeople.runninghi.oauth.dto.SigninResponseDto;
import com.fourpeople.runninghi.oauth.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@RequiredArgsConstructor
public class JWTManager implements JWTProperties{
    public static String makeToken(PrincipalDetails principal,int duration) {
        return getJwtBuilder()
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .withClaim("id", principal.getUser().getId())
                .withClaim("role", String.valueOf(principal.getUser().getRole()))
                .sign(Algorithm.HMAC512(SECRET));
    }

    public static String makeGuestToken(int duration) {
        return getJwtBuilder()
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .withClaim("role", String.valueOf(Role.GUEST))
                .sign(Algorithm.HMAC512(SECRET));
    }

    public static SigninResponseDto makeUserBothToken(PrincipalDetails principal) {
        return SigninResponseDto.of(makeToken(principal,ACCESS_TOKEN_DURATION), makeToken(principal,REFRESH_TOKEN_DURATION));
    }

    public static SigninResponseDto makeGuestBothToken() {
        return SigninResponseDto.of(makeGuestToken(ACCESS_TOKEN_DURATION), makeGuestToken(REFRESH_TOKEN_DURATION));
    }
    private static JWTCreator.Builder getJwtBuilder() {
        return JWT
                .create()
                .withIssuer(ISSUER);
    }

    public static boolean isValidJWT(String token) {
        try {
            JWTVerifier verifier = JWT
                    .require(Algorithm.HMAC512(SECRET))
                    .withIssuer(ISSUER)
                    .build();
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            System.out.println("not valid token");
            return false;
        }
        return true;
    }
}