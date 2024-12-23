package com.nhnacademy.book.point.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.point.controller.MemberPointController;
import com.nhnacademy.book.point.domain.PointConditionName;
import com.nhnacademy.book.point.dto.MemberPointAddRequestDto;
import com.nhnacademy.book.point.dto.MemberPointAddResponseDto;
import com.nhnacademy.book.point.repository.MemberPointRepository;
import com.nhnacademy.book.point.repository.PointConditionRepository;
import com.nhnacademy.book.point.service.MemberPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberPointServiceImplTest {

    @Mock
    private MemberPointService memberPointService;

    @InjectMocks
    private MemberPointController memberPointController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    private MemberPointAddRequestDto memberPointAddRequestDto;
    private MemberPointAddResponseDto memberPointAddResponseDto;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberPointController).build();

        // 테스트용 데이터
        memberPointAddRequestDto = new MemberPointAddRequestDto(
                1L,        // memberId
                null,       // reviewId, null일 수 있음
                PointConditionName.SIGN_UP,  // 포인트 조건명
                5000,       // 조건 포인트
                null        // conditionPercentage (null)
        );

        memberPointAddResponseDto = new MemberPointAddResponseDto(
                1L,        // memberPointId
                1L,        // memberId
                "SIGN_UP",  // 포인트 조건명
                new BigDecimal(5000),       // 적립된 포인트
                null,       // addDate
                null        // endDate
        );
    }


    @Test
    @DisplayName("회원가입 시 포인트 적립 성공")
    void addSignUpPoint() throws Exception {
        // given
        Long memberId = 1L;
        when(memberPointService.addSignUpPoint(eq(memberId))).thenReturn(memberPointAddResponseDto);

        // when & then
        mockMvc.perform(post("/api/members/" + memberId + "/points/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberPointAddRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(memberPointAddResponseDto)));
    }


}
