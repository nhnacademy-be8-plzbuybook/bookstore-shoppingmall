package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.SellingBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public interface SellingBookRepository extends JpaRepository<SellingBook, Long> {

    SellingBook findByBook(Book book);
    // 도서 상태별 조회 ㅇㅇ
    List<SellingBook> findBySellingBookStatus(SellingBook.SellingBookStatus status);

    //특정 가격 범위의 판매책 조회 ㅇㅇ
    List<SellingBook> findBySellingBookPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // 특정 조회수 이상/이하의 판매책 조회 ㅇㅇ
    List<SellingBook> findBySellingBookViewCountGreaterThanEqual(Long minViewCount);

    // 특정 도서(Book)와 연결된 판매 도서 조회 ㅇㅇ
//    List<SellingBook> findByBook_BookId(Long bookId);


    @Query("SELECT DISTINCT sb FROM SellingBook sb " +
            "JOIN sb.book b " +
            "JOIN b.bookCategories bc " +
            "JOIN bc.category c " +
            "WHERE c.categoryId = :categoryId " +
            "OR (c.parentCategory IS NOT NULL AND c.parentCategory.categoryId = :categoryId)")
    List<SellingBook> findByCategoryIdOrParent(@Param("categoryId") Long categoryId);

    //조회수가 일정 이상인 판매 도서를 페이징해서 가져오도록 메서드
    Page<SellingBook> findBySellingBookViewCountGreaterThanEqual(Long minViewCount, Pageable pageable);

    List<SellingBook> findByBook_BookId(Long bookId); // bookId를 기준으로 SellingBook 조회

    List<SellingBook> findByBook_BookIdIn(ArrayList<Long> longs);

    @Query("SELECT sb FROM SellingBook sb " +
            "LEFT JOIN Likes l ON l.sellingBook.sellingBookId = sb.sellingBookId " +
            "GROUP BY sb.sellingBookId " +
            "ORDER BY COUNT(l.likesId) DESC")
    Page<SellingBook> findAllWithLikeCount(Pageable pageable);

}
