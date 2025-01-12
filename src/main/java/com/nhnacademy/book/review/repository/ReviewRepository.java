package com.nhnacademy.book.review.repository;

import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByOrderProduct(OrderProduct orderProduct);

    //책 id를 통해 해당하는 리뷰 불러오기
    @Query("SELECT r FROM Review r WHERE r.orderProduct.sellingBook.sellingBookId = :bookId")
    List<Review> findReviewByBookId(Long bookId);
}
