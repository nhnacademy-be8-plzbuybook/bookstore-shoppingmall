package com.nhnacademy.book.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.point.dto.MemberPointListResponseDto;
import com.nhnacademy.book.point.dto.MemberPointUseRequestDto;
import com.nhnacademy.book.point.service.MemberPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class MemberPointControllerTest {

    @Mock
    private MemberPointService memberPointService;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberPointController memberPointController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        mockMvc = MockMvcBuilders.standaloneSetup(memberPointController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    @DisplayName("회원 포인트 목록 조회 성공")
    void getMemberPoints_Success() throws Exception {
        Long memberId = 1L;

        Member member = new Member();
        member.setMemberId(memberId);

        List<MemberPointListResponseDto> points = List.of(
                new MemberPointListResponseDto(1L, memberId, "BOOK_PURCHASE", 500, LocalDateTime.now(), LocalDateTime.now().plusYears(1), null, "SAVE")
        );
        when(memberRepository.findById(memberId)).thenReturn(java.util.Optional.of(new Member()));
        when(memberPointService.getMemberPointsByMemberId(memberId)).thenReturn(points);

        mockMvc.perform(get("/api/members/{member_id}/points", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("BOOK_PURCHASE"))
                .andExpect(jsonPath("$[0].point").value(500))
                .andExpect(jsonPath("$[0].type").value("SAVE"))
                .andExpect(jsonPath("$[0].addDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists());

    }

    @Test
    @DisplayName("회원 포인트 조회 실패 - 회원 없음")
    void getMemberPoints_MemberNotFound() throws Exception {
        // Mock 동작 설정
        Mockito.when(memberRepository.findById(anyLong())).thenThrow(new MemberNotFoundException("회원이 존재하지 않습니다."));

        // API 호출 및 검증
        mockMvc.perform(get("/api/members/1/points"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("사용가능한 포인트 조회 성공")
    void getAvailablePoints_Success() throws Exception {
        String email = "test@naver.com";
        int availablePoints = 10000;

        when(memberPointService.getAvailablePoints(email)).thenReturn(availablePoints);

        mockMvc.perform(post("/api/points/members/available")
                        .header("X-USER-ID", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(availablePoints));

        verify(memberPointService, times(1)).getAvailablePoints(email);
    }

    @Test
    @DisplayName("포인트 사용 성공")
    void usedPoint_Success() throws Exception {
        MemberPointUseRequestDto requestDto = new MemberPointUseRequestDto("user@example.com", 500);

        // API 호출 및 검증
        mockMvc.perform(post("/api/points/members/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("포인트 사용이 완료되었습니다."));

        // verify 사용으로 서비스 호출 검증
        verify(memberPointService, times(1)).usedPoint(requestDto.getEmail(), requestDto.getUsedPoint());
    }
}


