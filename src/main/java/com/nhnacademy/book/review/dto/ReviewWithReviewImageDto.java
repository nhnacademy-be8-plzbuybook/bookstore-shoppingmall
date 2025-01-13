package com.nhnacademy.book.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewWithReviewImageDto {
    private Long reviewId;
    private String email;
    private Long orderProductId;
    private LocalDateTime writeDate;
    private int score;
    private String content;
    private List<String> imageUrls;
}
