package com.nhnacademy.book.review.repository;

import com.nhnacademy.book.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    //리뷰 아이디를 통해 리뷰 이미지를 가져오기
    @Query("SELECT ri FROM ReviewImage ri WHERE ri.review.orderProduct.sellingBook.sellingBookId = :bookId")
    List<ReviewImage> findReviewImageByBookId(Long bookId);

    @Query("SELECT ri FROM ReviewImage ri WHERE ri.review.orderProduct.sellingBook.sellingBookId = :bookId AND ri.review.reviewId IN :reviewIds")
    List<ReviewImage> findImagesByBookIdAndReviewIds(Long bookId, List<Long> reviewIds);
}
