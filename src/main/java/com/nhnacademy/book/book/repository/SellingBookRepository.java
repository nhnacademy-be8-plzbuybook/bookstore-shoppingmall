package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.SellingBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface SellingBookRepository extends JpaRepository<SellingBook, Long> {

//    // 판매가가 높은 순에서 낮은 순으로 정렬 - 내림차순
//    List<SellingBook> findByOrderBySellingBookPriceDesc();
//
//    // 판매가가 낮은순 에서 높은순으로 정렬 - 오름차순
//    List<SellingBook> findByOrderBySellingBookPriceAsc();

     // Spring JPA의 Sort 객체를 활용하면 하나의 기본 메서드로 처리할수있음.

    // 도서 상태별 조회 ㅇㅇ
    List<SellingBook> findBySellingBookStatus(SellingBook.SellingBookStatus status);

    //특정 가격 범위의 판매책 조회 ㅇㅇ
    List<SellingBook> findBySellingBookPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // 특정 조회수 이상/이하의 판매책 조회 ㅇㅇ
    List<SellingBook> findBySellingBookViewCountGreaterThanEqual(Long minViewCount);

    // 특정 도서(Book)와 연결된 판매 도서 조회 ㅇㅇ
    List<SellingBook> findByBook_BookId(Long bookId);


//    @Query("SELECT DISTINCT sb FROM SellingBook sb " +
//            "JOIN sb.book b " +
//            "JOIN b.bookCategories bc " +
//            "JOIN bc.category c " +
//            "LEFT JOIN c.parentCategory parent " + // LEFT JOIN 추가
//            "WHERE c.categoryId = :categoryId OR parent.categoryId = :categoryId")
//    List<SellingBook> findByCategoryIdOrParent(@Param("categoryId") Long categoryId);


    @Query("SELECT DISTINCT sb FROM SellingBook sb " +
            "JOIN sb.book b " +
            "JOIN b.bookCategories bc " +
            "JOIN bc.category c " +
            "WHERE c.categoryId = :categoryId " +
            "OR (c.parentCategory IS NOT NULL AND c.parentCategory.categoryId = :categoryId)")
    List<SellingBook> findByCategoryIdOrParent(@Param("categoryId") Long categoryId);

}
