package com.nhnacademy.book.objectStorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequestDto {
    private OSAuth osAuth;

    @Getter
    @NoArgsConstructor // 기본 생성자 추가
    public static class OSAuth {
        private String tenantId;
        private PasswordCredentials passwordCredentials;

        @Builder
        public OSAuth(@JsonProperty("tenantId") String tenantId,
                    @JsonProperty("passwordCredentials") PasswordCredentials passwordCredentials) {
            this.tenantId = tenantId;
            this.passwordCredentials = passwordCredentials;
        }
    }

    @Getter
    @NoArgsConstructor // 기본 생성자 추가
    public static class PasswordCredentials {
        private String username;
        private String password;

        @Builder
        public PasswordCredentials(@JsonProperty("username") String username,
                                   @JsonProperty("password") String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Builder
    public TokenRequestDto(@JsonProperty("tenantId") String tenantId,
                           @JsonProperty("username") String username,
                           @JsonProperty("password") String password) {
        PasswordCredentials passwordCredentials =
                PasswordCredentials.builder()
                        .username(username)
                        .password(password)
                        .build();

        this.osAuth = OSAuth.builder()
                .tenantId(tenantId)
                .passwordCredentials(passwordCredentials)
                .build();
    }
}
