package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.SellingBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SellingBookRepository extends JpaRepository<SellingBook, Long> {

    // 판매가가 높은 순으로 정렬
    List<SellingBook> findByOrderBySellingBookPriceDesc();

    // 판매가가 낮은 순으로 정렬
    List<SellingBook> findByOrderBySellingBookPriceAsc();

    //조회수 높은순으로 정렬
    List<SellingBook> findByOrderBySellingBookViewCountDesc();

    //조회수 낮은순으로 정렬
    List<SellingBook> findByOrderBySellingBookViewCountAsc();


    // 도서 상태에 따른 판매 도서
    List<SellingBook> findBySellingBookStatus(SellingBook.SellingBookStatus status);


}
