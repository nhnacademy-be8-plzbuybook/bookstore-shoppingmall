package com.nhnacademy.book.member.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthResponseDto;
import com.nhnacademy.book.member.domain.service.MemberAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("회원의 권한 등록")
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
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON) // JSON 응답을 기대
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").value(responseDto.getMemberId()))
                .andExpect(jsonPath("$.authId").value(responseDto.getAuthId()));
    }

    @Test
    @DisplayName("권한에 해당하는 회원 조회")
    void getAuthsByMember() throws Exception {
        // Given
        Long memberId = 1L;
        List<String> authNames = Arrays.asList("ADMIN", "USER");
        when(memberAuthService.getAuthNameByMember(memberId)).thenReturn(authNames);

        // When & Then
        mockMvc.perform(get("/api/members/{member_id}/auths", memberId)
                        .accept(MediaType.APPLICATION_JSON) // JSON 응답을 기대
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("ADMIN"))
                .andExpect(jsonPath("$[1]").value("USER"));
    }

    @Test
    @DisplayName("회원권한 수정")
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
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON) // JSON 응답을 기대
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(responseDto.getMemberId()))
                .andExpect(jsonPath("$.authId").value(responseDto.getAuthId()));
    }

    @Test
    @DisplayName("회원권한 삭제")
    void deleteMemberAuth() throws Exception {
        Long memberId = 1L;
        Long authId = 101L;

        doNothing().when(memberAuthService).deleteAuthFromMember(memberId, authId);

        mockMvc.perform(delete("/api/members/{member_id}/auths/{auth_id}", memberId, authId))
                .andExpect(status().isNoContent());
    }

}




