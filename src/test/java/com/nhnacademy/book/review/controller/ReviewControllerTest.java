package com.nhnacademy.book.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @Test
    @DisplayName("리뷰 생성")
    void reviewCreate() throws Exception {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"Password");
        Long orderProductId = 1L;

        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto();
        requestDto.setScore(5);
        requestDto.setContent("good");
        ReviewResponseDto responseDto = new ReviewResponseDto(1L, member.getMemberId(), "윤지호", orderProductId, 5, "good", null, null);

        when(reviewService.createReview(eq(member.getEmail()), eq(orderProductId), any(ReviewCreateRequestDto.class))).thenReturn(responseDto);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/products/{product_Id}", orderProductId)
                .header("X-USER-ID", member.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviewId").value(responseDto.getReviewId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.memberId").value(member.getMemberId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderProductId").value(orderProductId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.score").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("good"));


    }
}