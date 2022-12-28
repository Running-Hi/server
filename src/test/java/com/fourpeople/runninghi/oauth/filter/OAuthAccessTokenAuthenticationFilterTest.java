package com.fourpeople.runninghi.oauth.filter;

import com.fourpeople.runninghi.common.utils.ApiUtils;
import com.fourpeople.runninghi.oauth.entity.OAuthAccessToken;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OAuthAccessTokenAuthenticationFilterTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("필터 테스트")
    void filter_test() {
        OAuthAccessToken oAuthAccessToken = new OAuthAccessToken("12313123", "kakao");

        webTestClient.post()
                .uri("/oauth/signin/kakao")
                .body(BodyInserters.fromValue(oAuthAccessToken))
                .exchange()

                .expectStatus()
                .is2xxSuccessful()

                .expectBody()
                .consumeWith(System.out::println);
    }
}