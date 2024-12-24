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
    private String memberName;
    private Long orderProductId;
    private Integer score;
    private String content;
    private LocalDateTime writeDate;
    private LocalDateTime modifiedDate;
}
