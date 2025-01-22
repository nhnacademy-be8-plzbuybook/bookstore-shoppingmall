package com.nhnacademy.book.member.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.member.domain.dto.certification.*;
import com.nhnacademy.book.member.domain.service.MemberCertificationService;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberCertificationController.class)
class MemberCertificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberCertificationService memberCertificationService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("인증 생성 성공")
    void createCertification() throws Exception {
        CertificationCreateRequestDto requestDto = new CertificationCreateRequestDto();
        requestDto.setMemberId(1L);
        requestDto.setCertification("일반");

        CertificationCreateResponseDto responseDto = new CertificationCreateResponseDto();
        responseDto.setMemberAuthId(1L);
        responseDto.setMemberId(1L);
        responseDto.setCertification("일반");

        when(memberCertificationService.addCertification(any(CertificationCreateRequestDto.class)))
                .thenReturn(responseDto);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/certification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberAuthId").value(1L))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.certification").value("일반"));
    }

    @Test
    @DisplayName("인증 조회")
    void getCertification() throws Exception {
        Long memberId = 1L;

        CertificationResponseDto certificationResponseDto = new CertificationResponseDto();
        certificationResponseDto.setMemberId(memberId);
        certificationResponseDto.setCertification("일반");

        when(memberCertificationService.getCertificationByMemberId(memberId)).thenReturn(certificationResponseDto);

        mockMvc.perform(get("/api/members/{member_id}/certification", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.certification").value("일반"));
    }

    @Test
    @DisplayName("인증 수정")
    void updateCertification() throws Exception {
        Long memberId = 1L;

        CertificationUpdateRequestDto requestDto = new CertificationUpdateRequestDto();
        requestDto.setCertification("페이코");

        CertificationUpdateResponseDto responseDto = new CertificationUpdateResponseDto();
        responseDto.setMemberId(memberId);
        responseDto.setCertification("페이코");

        when(memberCertificationService.updateCertificationMethod(eq(memberId), any(CertificationUpdateRequestDto.class)))
                .thenReturn(responseDto);

        String requestJson = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(patch("/api/members/{member_id}/certification", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.certification").value("페이코"));
    }

    @Test
    @DisplayName("마지막 로그인 시간 갱신")
    void updateLastLogin() throws Exception {
        UpdateLastLoginRequestDto updateLastLoginRequestDto = new UpdateLastLoginRequestDto();
        updateLastLoginRequestDto.setMemberId(1L);

        UpdateLastLoginResponseDto updateLastLoginResponseDto = new UpdateLastLoginResponseDto();
        updateLastLoginResponseDto.setMemberId(1L);
        updateLastLoginResponseDto.setLastLogin(LocalDateTime.of(2000,3,9,1,1,1));

        when(memberCertificationService.updateLastLogin(any(UpdateLastLoginRequestDto.class))).thenReturn(updateLastLoginResponseDto);

        String requestJson = objectMapper.writeValueAsString(updateLastLoginRequestDto);
        mockMvc.perform(post("/api/members/{member_id}/last_login", updateLastLoginRequestDto.getMemberId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(updateLastLoginRequestDto.getMemberId()))
                .andExpect(jsonPath("$.lastLogin").value("2000-03-09T01:01:01"));

    }

    @Test
    @DisplayName("인증 방식 삭제")
    void deleteCertification() throws Exception {
        DeleteCertificationRequestDto deleteCertificationRequestDto = new DeleteCertificationRequestDto();
        deleteCertificationRequestDto.setMemberId(1L);

        DeleteCertificationResponseDto deleteCertificationResponseDto = new DeleteCertificationResponseDto();
        deleteCertificationResponseDto.setSuccess(true);
        deleteCertificationResponseDto.setMessage("삭제 성공!");

        when(memberCertificationService.deleteCertification(any(DeleteCertificationRequestDto.class))).thenReturn(deleteCertificationResponseDto);

        String requestJson = objectMapper.writeValueAsString(any(DeleteCertificationRequestDto.class));
        mockMvc.perform(delete("/api/members/{member_id}/certification", deleteCertificationRequestDto.getMemberId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value("삭제 성공!"));


    }

    @Test
    @DisplayName("모든 회원의 인증 방식 조회")
    void getAllCertification() throws Exception {

        List<CertificationResponseDto> certificationResponseDtoList = List.of(
                new CertificationResponseDto(1L, "test1", "일반"),
                new CertificationResponseDto(2L, "test2", "페이코"));
        when(memberCertificationService.getAllCertifications()).thenReturn(certificationResponseDtoList);

        mockMvc.perform(get("/api/members/certification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].memberId").value(1L))
                .andExpect(jsonPath("$[0].memberName").value("test1"))
                .andExpect(jsonPath("$[0].certification").value("일반"))
                .andExpect(jsonPath("$[1].memberId").value(2L))
                .andExpect(jsonPath("$[1].memberName").value("test2"))
                .andExpect(jsonPath("$[1].certification").value("페이코"));

        verify(memberCertificationService, times(1)).getAllCertifications();

    }

    @Test
    @DisplayName("마지막 로그인 일시 갱신 테스트")
    void updateLastLoginByEmail_Success() throws Exception {
        LastLoginRequestDto requestDto = new LastLoginRequestDto();
        requestDto.setEmail("test@naver.com");

        LastLoginResponseDto responseDto = new LastLoginResponseDto();
        responseDto.setEmail("test@naver.com");
        responseDto.setLastLogin(LocalDateTime.of(2025, 1, 21, 13, 30));

        when(memberCertificationService.updateLastLoginByEmail(any(LastLoginRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/members/last-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@naver.com"))
                .andExpect(jsonPath("$.lastLogin").value("2025-01-21T13:30:00"));

    }
}