package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // ISBN 13으로 책 조회
    Book findByBookIsbn13(String bookIsbn13);

    // 중복 ISBN 13 체크
    boolean existsByBookIsbn13(String bookIsbn13);

    // 제목으로 책 조회
    List<Book> findByBookTitle(String bookTitle);



    // 제목에 특정 문자열이 포함된 책 조회
    List<Book> findByBookTitleContaining(String bookTitle);

    // index에 특정 문자열 조회
    List<Book> findByBookIndexContaining(String bookIndex);

    // 특정 출판사의 책 조회
    List<Book> findByPublisher_PublisherId(Long publisherId);

    // 특정 카테고리에 속한 책 조회
    List<Book> findByBookCategories_Category_CategoryId(Long categoryId);

//    // 책을 쓴 작가를 검색
//    @Query("SELECT a FROM Author a JOIN a.bookAuthors ba JOIN ba.book b WHERE b.bookId = :bookId")
//    List<Author> findAuthorsByBookId(@Param("bookId") Long bookId);
//
//    // 작가가 집필한 책 검색
//    @Query("SELECT b FROM Book b JOIN b.bookAuthors ba JOIN ba.author a WHERE a.authorName = :authorName")
//    List<Book> findBooksByAuthorName(@Param("authorName") String authorName);


}
