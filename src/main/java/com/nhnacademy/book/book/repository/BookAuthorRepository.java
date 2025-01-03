package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.elastic.document.BookAuthorDocument;
import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookAuthor;
import com.nhnacademy.book.book.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {


    //작가가 쓴 책 모두 조회
    @Query("SELECT ba.book FROM BookAuthor ba WHERE ba.author.authorId = :authorId")
    List<Book> findBooksByAuthorId(@Param("authorId") Long authorId);

    //책을 쓴 작가 모두 조회
    @Query("SELECT ba.author FROM BookAuthor ba WHERE ba.book.bookId = :bookId")
    List<Author> findAuthorsByBookId(@Param("bookId") Long bookId);

    List<BookAuthor> findByBook_BookId(Long bookId);

    List<BookAuthor> findByBook_BookIdIn(List<Long> longs);


    // Book과 연결된 카테고리를 조회하는 메서드
//    @Query("SELECT c FROM Category c JOIN c.bookCategories bc WHERE bc.book.bookId = :bookId")
//    List<Category> findCategoriesByBookId(@Param("bookId") Long bookId);
}
