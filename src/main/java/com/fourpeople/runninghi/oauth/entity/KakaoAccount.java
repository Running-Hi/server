package com.fourpeople.runninghi.oauth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class KakaoAccount {
    @JsonProperty("email")
    private String email;
}
