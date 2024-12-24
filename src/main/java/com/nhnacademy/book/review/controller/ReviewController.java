package com.nhnacademy.book.review.controller;

import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    //ID(이메일) : 이메일은 gateway에서 검증해준 x-user-Id를 통해 가져온다
    //product_Id = order_productId
    @PostMapping("/products/{product_Id}")
    public ResponseEntity<ReviewResponseDto> createReview(@RequestHeader("X-USER-ID") String userId, @PathVariable Long product_Id, @RequestBody ReviewCreateRequestDto reviewRequestDto) {
        ReviewResponseDto reviewResponseDto = reviewService.createReview(userId, product_Id, reviewRequestDto);
        return ResponseEntity.ok(reviewResponseDto);
    }
}
