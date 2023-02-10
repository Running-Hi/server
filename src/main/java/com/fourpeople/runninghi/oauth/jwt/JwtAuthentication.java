package com.fourpeople.runninghi.oauth.jwt;

import lombok.Getter;

import static lombok.Lombok.checkNotNull;

@Getter
public class JwtAuthentication {
    private final Long id;

    public JwtAuthentication(Long id) {
        checkNotNull(id, "id must be provided");
        this.id = id;
    }
}