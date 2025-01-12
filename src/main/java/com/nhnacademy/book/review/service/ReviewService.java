package com.nhnacademy.book.review.service;

import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.dto.ReviewUpdateRequestDto;
import com.nhnacademy.book.review.dto.ReviewWithReviewImageDto;

import java.util.List;

public interface ReviewService {

    //특정 회원이 리뷰 작성
    ReviewResponseDto createReview(ReviewCreateRequestDto createRequestDto, List<String> imageUrls);

    //책에 대한 리뷰 조회
    List<ReviewWithReviewImageDto> getReviewsWithReviewImagesByBookId(Long BookId);

    //별점 평균
    Double averageRatingByBookId(Long BookId);


}
