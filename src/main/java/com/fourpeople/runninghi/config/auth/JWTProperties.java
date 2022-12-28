
package com.fourpeople.runninghi.config.auth;

public interface JWTProperties {
    String ISSUER = "Running-hi";
    int ACCESS_TOKEN_DURATION = 1000 * 60 * 10; // 10분
    int REFRESH_TOKEN_DURATION = 60 * 1000 * 60 * 10; // 600분
    String SECRET = "79008129-a00b-4476-8d14-efc25c754e44";
    String HEADER_KEY = "AUTHORIZATION";
    String PREFIX = "Bearer ";
}
