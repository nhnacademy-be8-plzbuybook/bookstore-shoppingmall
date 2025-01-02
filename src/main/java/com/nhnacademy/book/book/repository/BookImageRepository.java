package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {
    Optional<BookImage> findByBook(Book book); // Book으로 이미지 조회

    List<BookImage> findByBook_BookIdIn(ArrayList<Long> longs);
}
