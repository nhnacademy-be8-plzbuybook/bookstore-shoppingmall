package com.nhnacademy.book.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private Long memberId;
    private Long orderProductId;
    private int score;
    private String content;
}
