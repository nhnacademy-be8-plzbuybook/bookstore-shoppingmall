package com.nhnacademy.book.member.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthResponseDto;
import com.nhnacademy.book.member.domain.service.MemberAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberAuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MemberAuthService memberAuthService;

    @InjectMocks
    private MemberAuthController memberAuthController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberAuthController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void assignAuthToMember() throws Exception {
        MemberAuthRequestDto requestDto = new MemberAuthRequestDto();
        requestDto.setMemberId(1L);
        requestDto.setAuthId(101L);

        MemberAuthResponseDto responseDto = new MemberAuthResponseDto();
        responseDto.setMemberId(1L);
        responseDto.setAuthId(101L);

        when(memberAuthService.assignAuthToMember(any(MemberAuthRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/members/auths")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").value(responseDto.getMemberId()))
                .andExpect(jsonPath("$.authId").value(responseDto.getAuthId()));
    }

    @Test
    void getAuthsByMember() throws Exception {
        // Given
        Long memberId = 1L;
        List<Long> auths = Arrays.asList(101L, 102L);
        when(memberAuthService.getAuthsByMember(memberId)).thenReturn(auths);

        // When & Then
        mockMvc.perform(get("/api/members/{member_id}/auths", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(101))
                .andExpect(jsonPath("$[1]").value(102));
    }

    @Test
    void updateMemberAuth() throws Exception {
        // Given
        Long memberId = 1L;
        MemberAuthRequestDto requestDto = new MemberAuthRequestDto();
        requestDto.setMemberId(memberId);
        requestDto.setAuthId(101L);  // 수정하려는 권한 ID

        MemberAuthResponseDto responseDto = new MemberAuthResponseDto();
        responseDto.setMemberId(memberId);
        responseDto.setAuthId(101L);  // 응답으로 반환될 권한 ID

        when(memberAuthService.updateMemberAuth(any(MemberAuthRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(put("/api/members/{member_id}/auths", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(responseDto.getMemberId()))
                .andExpect(jsonPath("$.authId").value(responseDto.getAuthId()));
    }

    @Test
    void deleteMemberAuth() throws Exception {
        Long memberId = 1L;
        Long authId = 101L;

        doNothing().when(memberAuthService).deleteAuthFromMember(memberId, authId);

        mockMvc.perform(delete("/api/members/{member_id}/auths/{auth_id}", memberId, authId))
                .andExpect(status().isNoContent());
    }

}




