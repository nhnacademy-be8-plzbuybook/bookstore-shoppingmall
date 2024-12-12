package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByBookIsbn(String bookIsbn);

    Book findByBookTitle(String bookTitle);
}
