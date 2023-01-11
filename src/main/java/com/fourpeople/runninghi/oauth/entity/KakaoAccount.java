package com.fourpeople.runninghi.oauth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@ToString
public class KakaoAccount {
    @JsonProperty("email")
    private String email;
    @JsonProperty("has_email")
    private Boolean hasEmail;
}
