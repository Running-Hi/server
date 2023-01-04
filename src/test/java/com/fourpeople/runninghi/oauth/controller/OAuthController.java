package com.fourpeople.runninghi.oauth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourpeople.runninghi.config.auth.JWTManager;
import com.fourpeople.runninghi.oauth.entity.KakaoAccount;
import com.fourpeople.runninghi.oauth.entity.OAuthAccessToken;
import com.fourpeople.runninghi.oauth.service.OAuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static com.fourpeople.runninghi.common.utils.ApiUtils.success;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class OAuthController {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OAuthService oAuthService;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private ResponseEntity<KakaoAccount> exchange;

    @Test
    @DisplayName("성공: 카카오 OAuth 로그인")
    void SUCCESS_KAKAO_OAUTH_LOGIN() throws Exception {
        KakaoAccount kakaoAccount = new KakaoAccount("tmddn645@naver.com");
        ObjectMapper objectMapper = new ObjectMapper();

        doReturn(exchange)
                .when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), (Class<Object>) any());
        doReturn(kakaoAccount)
                .when(exchange).getBody();
        doReturn(success(JWTManager.makeGuestBothToken()))
                .when(oAuthService).signin(anyString());

        mockMvc.perform(post("/oauth/signin/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OAuthAccessToken("123123"))))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").exists())
                .andExpect(jsonPath("$.error").doesNotExist())

                .andDo(print());
    }
}