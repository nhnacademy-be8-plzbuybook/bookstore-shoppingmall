package com.nhnacademy.book.review.service;

import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.dto.ReviewUpdateRequestDto;
import com.nhnacademy.book.review.dto.ReviewWithReviewImageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {

    //특정 회원이 리뷰 작성
    ReviewResponseDto createReview(ReviewCreateRequestDto createRequestDto, List<String> imageUrls);

    //책에 대한 리뷰 조회
    Page<ReviewWithReviewImageDto> getReviewsWithReviewImagesByBookId(Long BookId, Pageable pageable);

    //별점 평균
    Double averageRatingByBookId(Long BookId);

    //리뷰 수정
    void updateReview(Long reviewId, ReviewUpdateRequestDto updateRequestDto, List<String> imageUrls);


}
