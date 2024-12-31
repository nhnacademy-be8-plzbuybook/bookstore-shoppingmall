package com.nhnacademy.book.review.repository;

import com.nhnacademy.book.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    boolean existsByReview_ReviewId(Long reviewId);
}
