package com.fourpeople.runninghi.oauth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class KakaoAccount {
    @JsonProperty("email")
    private final String email;
}
