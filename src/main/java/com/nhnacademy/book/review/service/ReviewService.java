package com.nhnacademy.book.review.service;

import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.dto.ReviewUpdateRequestDto;

import java.util.List;

public interface ReviewService {

    //특정 회원이 리뷰 작성(productId = order_product_id)
    ReviewResponseDto createReview(ReviewCreateRequestDto createRequestDto, List<String> imageUrls);

    //특정 상품에 대한 모든 리뷰 조회
    List<ReviewResponseDto> getReviewsByProductId(Long productId);

    //특정 회원이 작성한 리뷰 조회
    List<ReviewResponseDto> getReviewsByMemberId(Long memberId);

    //특정 리뷰 조회
    ReviewResponseDto getReviewById(Long reviewId);

    //리뷰 수정
    ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto);

    //리뷰 평점 계산
    Double calculateAverageScoreByProductId(Long productId);

    //전체 리뷰 조회
    List<ReviewResponseDto> getAllReviews();

}
