package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
}
