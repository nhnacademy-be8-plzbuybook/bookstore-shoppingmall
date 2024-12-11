package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
