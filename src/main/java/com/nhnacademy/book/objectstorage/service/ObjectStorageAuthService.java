package com.nhnacademy.book.objectstorage.service;

import com.nhnacademy.book.objectstorage.config.ObjectStorageConfig;
import com.nhnacademy.book.objectstorage.dto.TokenRequestDto;
import com.nhnacademy.book.objectstorage.exception.ObjectStorageAuthException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ObjectStorageAuthService {

    private final RestTemplate restTemplate;
    private final String authUrl;
    private final String tenantId;
    private final String username;
    private final String password;

    public ObjectStorageAuthService(ObjectStorageConfig objectStorageConfig, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.authUrl = objectStorageConfig.getAuthUrl();
        this.tenantId = objectStorageConfig.getTenantId();
        this.username = objectStorageConfig.getUserName();
        this.password = objectStorageConfig.getPassword();
    }


    public String requestToken() {
        String identityUrl = authUrl + "/tokens";
        HttpEntity<TokenRequestDto> httpEntity = buildHttpEntity();

        try {
            ResponseEntity<String> response = restTemplate.exchange(identityUrl, HttpMethod.POST, httpEntity, String.class);

            validateResponse(response);
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ObjectStorageAuthException("Fail to get request token: ");
        } catch (Exception ex) {
            throw new ObjectStorageAuthException("unexpected error request token");
        }
    }

    private HttpEntity<TokenRequestDto> buildHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        TokenRequestDto tokenRequestDto = TokenRequestDto.builder()
                .tenantId(tenantId)
                .username(username)
                .password(password)
                .build();

        return new HttpEntity<>(tokenRequestDto, headers);
    }

    private void validateResponse(ResponseEntity<String> response) {
        if (response.getStatusCode() != HttpStatus.OK || Objects.requireNonNull(response.getBody()).isEmpty()) {
            throw new ObjectStorageAuthException("Invalid response from object storage auth token service: status="
                    + response.getStatusCode());
        }
    }
}
