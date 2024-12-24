package com.nhnacademy.book.review.repository;

import com.nhnacademy.book.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
