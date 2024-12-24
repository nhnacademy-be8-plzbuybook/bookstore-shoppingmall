package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.BookDocument;
import com.nhnacademy.book.book.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, Long> {

    BookDocument findByBookId(Long bookId);

    // ISBN 13으로 책 조회
    BookDocument findByBookIsbn13(String bookIsbn13);

    // 중복 ISBN 13 체크
    boolean existsByBookIsbn13(String bookIsbn13);

    // 제목으로 책 조회
    List<BookDocument> findByBookTitle(String bookTitle);

    // 제목에 특정 문자열이 포함된 책 조회
    List<BookDocument> findByBookTitleContaining(String bookTitle);

    // index에 특정 문자열 조회
    List<BookDocument> findByBookIndexContaining(String bookIndex);

    // 특정 출판사의 책 조회
    List<BookDocument> findByPublisher_PublisherId(Long publisherId);

    // 특정 카테고리에 속한 책 조회
    List<BookDocument> findByBookCategories_Category_CategoryId(Long categoryId);


}
