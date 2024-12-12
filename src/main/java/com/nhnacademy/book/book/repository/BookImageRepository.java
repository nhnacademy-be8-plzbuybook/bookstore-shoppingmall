package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {
}
