package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.SellingBookDocument;
import com.nhnacademy.book.book.entity.SellingBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface SellingBookSearchRepository extends JpaRepository<SellingBookDocument, Long> {

    // 도서 상태별 조회 ㅇㅇ
    List<SellingBookDocument> findBySellingBookStatus(SellingBook.SellingBookStatus status);

    //특정 가격 범위의 판매책 조회 ㅇㅇ
    List<SellingBookDocument> findBySellingBookPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // 특정 조회수 이상/이하의 판매책 조회 ㅇㅇ
    List<SellingBookDocument> findBySellingBookViewCountGreaterThanEqual(Long minViewCount);

    // 특정 도서(Book)와 연결된 판매 도서 조회 ㅇㅇ
    List<SellingBookDocument> findByBook_BookId(Long bookId);

    @Query("SELECT DISTINCT sb FROM SellingBook sb " +
            "JOIN sb.book b " +
            "JOIN b.bookCategories bc " +
            "JOIN bc.category c " +
            "WHERE c.categoryId = :categoryId " +
            "OR (c.parentCategory IS NOT NULL AND c.parentCategory.categoryId = :categoryId)")
    List<SellingBookDocument> findByCategoryIdOrParent(@Param("categoryId") Long categoryId);

}
