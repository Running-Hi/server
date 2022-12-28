package com.fourpeople.runninghi.oauth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OAuthAccessToken {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("provider")
    private String provider;
    @JsonIgnore
    private static final String PREFIX = "Bearer ";
    @JsonIgnore
    public String getAuthorizationValue() {
        return PREFIX + accessToken;
    }
}
