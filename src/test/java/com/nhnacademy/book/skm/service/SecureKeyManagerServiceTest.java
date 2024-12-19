package com.nhnacademy.book.skm.service;

import com.nhnacademy.book.skm.dto.KeyResponseDto;
import com.nhnacademy.book.skm.dto.KeyResponseDto.Body;
import com.nhnacademy.book.skm.exception.KeyMangerException;
import com.nhnacademy.book.skm.properties.SKMProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URI;
import java.security.KeyStore;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class SecureKeyManagerServiceTest {

    private SecureKeyManagerService secureKeyManagerService;
    private SKMProperties skmProperties;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws Exception {
        // SKMProperties Mock 설정
        skmProperties = Mockito.mock(SKMProperties.class);
        when(skmProperties.getKeystoreFile()).thenReturn("skm.p12");
        when(skmProperties.getPassword()).thenReturn("4444");
        when(skmProperties.getUrl()).thenReturn("https://api-keymanager.nhncloudservice.com");
        when(skmProperties.getAppKey()).thenReturn("qTQNj7LyHhdAazH3");

        restTemplate = Mockito.mock(RestTemplate.class);

        secureKeyManagerService = new SecureKeyManagerService(skmProperties);
    }

    @Test
    @DisplayName("url secretFetch 성공")
    void fetchSecret_url_Success() {
        //KeyResponseDto Mock 설정
        KeyResponseDto responseDto = new KeyResponseDto();
        Body body = new Body();
        body.setSecret("jdbc:mysql://133.186.241.167:3306/project_be8_plzbuybook_bookstore");
        responseDto.setBody(body);

        ResponseEntity<KeyResponseDto> responseEntity = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(restTemplate.exchange(
                any(URI.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(KeyResponseDto.class))
        ).thenReturn(responseEntity);

        String secret = secureKeyManagerService.fetchSecret("1117490ed9294c8798e83f2cb162982d");

        assertNotNull(secret);
        assertEquals("jdbc:mysql://133.186.241.167:3306/project_be8_plzbuybook_bookstore", secret);
    }

    @Test
    @DisplayName("username secretFetch 성공")
    void fetchSecret_username_Success() {
        //KeyResponseDto Mock 설정
        KeyResponseDto responseDto = new KeyResponseDto();
        Body body = new Body();
        body.setSecret("project_be8_plzbuybook");
        responseDto.setBody(body);

        ResponseEntity<KeyResponseDto> responseEntity = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(restTemplate.exchange(
                any(URI.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(KeyResponseDto.class))
        ).thenReturn(responseEntity);

        String secret = secureKeyManagerService.fetchSecret("7f2f713f0a6c4d82acb64ed3aa831cee");

        assertNotNull(secret);
        assertEquals("project_be8_plzbuybook", secret);
    }

    @Test
    @DisplayName("password secretFetch 성공")
    void fetchSecret_password_Success() {
        //KeyResponseDto Mock 설정
        KeyResponseDto responseDto = new KeyResponseDto();
        Body body = new Body();
        body.setSecret("MOW6c#y4TVxi1P5b");
        responseDto.setBody(body);

        ResponseEntity<KeyResponseDto> responseEntity = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(restTemplate.exchange(
                any(URI.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(KeyResponseDto.class))
        ).thenReturn(responseEntity);

        String secret = secureKeyManagerService.fetchSecret("7ac2c65487d34a7fa73997fada11d730");

        assertNotNull(secret);
        assertEquals("MOW6c#y4TVxi1P5b", secret);
    }

    @Test
    @DisplayName("dto내용이 null인 경우 exception")
    void fetchSecret_dtoNull() throws Exception {
        //KeyResponseDto 내용이 null인 경우
        ResponseEntity<KeyResponseDto> responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        when(restTemplate.exchange(
                any(URI.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(KeyResponseDto.class))
        ).thenReturn(responseEntity);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            secureKeyManagerService.fetchSecret("testKeyId");
        });

        assertTrue(exception.getMessage().contains("Invalid response from Key Manager"));
    }

    @Test
    @DisplayName("예외 처리 확인")
    void fetchSecret_exception() throws Exception {
        //keystore 읽기 중 IOException 강제 발생
        when(skmProperties.getKeystoreFile()).thenThrow(new KeyMangerException("Keystore file not found."));

        Exception exception = assertThrows(KeyMangerException.class, () -> {
            secureKeyManagerService.fetchSecret("testKeyId");
        });

        assertTrue(exception.getMessage().contains("Keystore file not found."));
    }
}
