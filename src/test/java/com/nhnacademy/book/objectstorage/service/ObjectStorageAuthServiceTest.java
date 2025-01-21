//package com.nhnacademy.book.objectstorage.service;
//
//import com.nhnacademy.book.objectstorage.config.ObjectStorageConfig;
//import com.nhnacademy.book.objectstorage.dto.TokenRequestDto;
//import com.nhnacademy.book.objectstorage.exception.ObjectStorageAuthException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class ObjectStorageAuthServiceTest {
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @Mock
//    private ObjectStorageConfig objectStorageConfig;
//
//
//    private ObjectStorageAuthService objectStorageAuthService;
//
//    @BeforeEach
//    void setUp() {
//        when(objectStorageConfig.getAuthUrl()).thenReturn("http://mock-auth-url.com");
//        when(objectStorageConfig.getTenantId()).thenReturn("mock-tenant-id");
//        when(objectStorageConfig.getUserName()).thenReturn("mock-username");
//        when(objectStorageConfig.getPassword()).thenReturn("mock-password");
//
//        objectStorageAuthService = new ObjectStorageAuthService(objectStorageConfig, restTemplate);
//    }
//
//    @Test
//    @DisplayName("정상적으로 토큰이 반환된다")
//    void return_request_token() {
//        String expectedTokenResponse = "mock-token-response";
//        String identityUrl = "http://mock-auth-url.com/tokens";
//
//        TokenRequestDto tokenRequestDto = TokenRequestDto.builder()
//                .tenantId("mock-tenant-id")
//                .username("mock-username")
//                .password("mock-password")
//                .build();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//
//        HttpEntity<TokenRequestDto> requestEntity = new HttpEntity<>(tokenRequestDto, headers);
//
//        when(restTemplate.exchange(
//                eq(identityUrl),
//                eq(HttpMethod.POST),
//                any(HttpEntity.class),
//                eq(String.class)
//        )).thenReturn(new ResponseEntity<>(expectedTokenResponse, HttpStatus.OK));
//
//        String actualTokenResponse = objectStorageAuthService.requestToken();
//
//        assertEquals(expectedTokenResponse, actualTokenResponse);
//    }
//
//    @Test
//    @DisplayName("RestTemplate이 오류 상태를 반환하면 예외가 발생해야 한다")
//    void restTemplate_exception() {
//        String identityUrl = "http://mock-auth-url.com/tokens";
//
//        when(restTemplate.exchange(
//                eq(identityUrl),
//                eq(HttpMethod.POST),
//                any(HttpEntity.class),
//                eq(String.class)
//        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
//
//        assertThrows(ObjectStorageAuthException.class, () -> objectStorageAuthService.requestToken());
//    }
//
//    @Test
//    @DisplayName("토큰이 empty일떄 예외를 반환해야 한다")
//    void token_empty_exception() {
//        // Given
//        String identityUrl = "http://mock-auth-url.com/tokens";
//
//        when(restTemplate.exchange(
//                eq(identityUrl),
//                eq(HttpMethod.POST),
//                any(HttpEntity.class),
//                eq(String.class)
//        )).thenReturn(new ResponseEntity<>("", HttpStatus.OK));
//
//        Exception exception = assertThrows(ObjectStorageAuthException.class, () -> objectStorageAuthService.requestToken());
//    }
//
//}