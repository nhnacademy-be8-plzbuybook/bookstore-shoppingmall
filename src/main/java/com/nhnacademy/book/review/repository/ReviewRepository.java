package com.nhnacademy.book.review.repository;

import com.nhnacademy.book.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMember_MemberId(Long memberId);
}
