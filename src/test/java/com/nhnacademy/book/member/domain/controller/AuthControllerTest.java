package com.nhnacademy.book.member.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.member.domain.dto.auth.AuthRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.AuthResponseDto;
import com.nhnacademy.book.member.domain.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createAuth() throws Exception {
        // Given
        AuthRequestDto requestDto = new AuthRequestDto("ADMIN");
        AuthResponseDto responseDto = new AuthResponseDto(1L, "ADMIN");
        when(authService.createAuth("ADMIN")).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/auths")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authId").value(1))
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    void getAllAuths() throws Exception {

        AuthResponseDto responseDto = new AuthResponseDto(1L, "ADMIN");
        AuthResponseDto responseDto2 = new AuthResponseDto(2L, "USER");
        when(authService.getAllAuths()).thenReturn(Arrays.asList(responseDto, responseDto2));

        mockMvc.perform(get("/api/auths"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authId").value(1))
                .andExpect(jsonPath("$[0].name").value("ADMIN"))
                .andExpect(jsonPath("$[1].authId").value(2))
                .andExpect(jsonPath("$[1].name").value("USER"));
    }

    @Test
    void getAuthById() throws Exception {
        // Given
        AuthResponseDto responseDto = new AuthResponseDto(1L, "ADMIN");
        when(authService.getAuthById(1L)).thenReturn(Optional.of(responseDto));

        // When & Then
        mockMvc.perform(get("/api/auths/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authId").value(1))
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }
    @Test
    void getAuthById_notFound() throws Exception {
        // Given
        when(authService.getAuthById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/auths/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAuth() throws Exception {
        // Given
        AuthRequestDto requestDto = new AuthRequestDto("SUPER_ADMIN");
        AuthResponseDto responseDto = new AuthResponseDto(1L, "SUPER_ADMIN");
        when(authService.updateAuth(1L, "SUPER_ADMIN")).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(put("/api/auths/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authId").value(1))
                .andExpect(jsonPath("$.name").value("SUPER_ADMIN"));
    }

    @Test
    void deleteAuth() throws Exception {
        // Given
        doNothing().when(authService).deleteAuth(1L);

        // When & Then
        mockMvc.perform(delete("/api/auths/1"))
                .andExpect(status().isNoContent());
    }

}



