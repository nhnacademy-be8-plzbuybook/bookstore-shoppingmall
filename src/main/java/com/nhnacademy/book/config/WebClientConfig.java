package com.nhnacademy.book.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@Slf4j
@Profile("!test") // 'test' 프로파일이 아닐 때만 실행
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        //TODO 지금은 로컬 주소니까 나중에 서버 주소로 바꿔주면 되겠다
        return builder.baseUrl("http://localhost:8090").build();
    }

    @Bean
    public ApplicationRunner apiCallOnStartup(WebClient webClient) {
        return args -> {
            String apiUrl = "/api/books/sync";

            webClient.post()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(response -> log.debug("API Response: {}", response))
                    .doOnError(error -> log.error("API Error: {}", error.getMessage()))
                    .block(); // 블로킹으로 처리하여 애플리케이션 시작 시 호출 완료

        };
    }
}
