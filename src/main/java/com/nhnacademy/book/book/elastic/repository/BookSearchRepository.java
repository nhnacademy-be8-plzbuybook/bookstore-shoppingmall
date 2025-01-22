package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.BookDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, Long> {

    @Query("{\"match\": {\"book_title\": \"?0\"}}")
    BookDocument findByBookId(Long bookId);

    // ISBN 13으로 책 조회
    BookDocument findByBookIsbn13(String bookIsbn13);

    // 중복 ISBN 13 체크
    boolean existsByBookIsbn13(String bookIsbn13);

    // 제목으로 책 조회
    List<BookDocument> findByBookTitle(String bookTitle);


    @Query("{\"match\": {\"book_title\": {\"query\": \"?0\", \"operator\": \"or\"}}}")
    List<BookDocument> findByBookTitleContaining(String keyword);


    // index에 특정 문자열 조회
    List<BookDocument> findByBookIndexContaining(String bookIndex);

    @Query("""
    {
      "query": {
        "bool": {
          "should": [
            { "match_phrase": { "authorName": "?0" } },
            { "match_phrase": { "bookTitle": "?0" } },
            { "match_phrase": { "categoryName": "?0" } }
          ]
        }
      }
    }
    """)
    Page<BookDocument> searchBooksByKeyword(String searchKeyword, Pageable pageable);




    List<BookDocument> findByBookIdIn(List<Long> bookIds);
}
