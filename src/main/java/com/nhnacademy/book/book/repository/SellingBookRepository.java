package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.SellingBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SellingBookRepository extends JpaRepository<SellingBook, Long> {


}
