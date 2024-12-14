package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByBookIsbn(String bookIsbn);

    List<Book> findByBookTitle(String bookTitle);

    // 1. 책에서 작가 검색 (작가 이름으로 책을 찾기)
    List<Book> findByBookAuthors_Author_AuthorName(String authorName);

    // 2. 작가가 집필한 책 검색 (책 제목으로 작가 검색)
    @Query("SELECT b FROM Book b JOIN b.bookAuthors ba JOIN ba.author a WHERE a.authorName = :authorName")
    List<Book> findBooksByAuthorName(@Param("authorName") String authorName);


}
