package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.BookAuthorDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookAuthorSearchRepository extends ElasticsearchRepository<BookAuthorDocument, Long> {

    // 작가가 쓴 책 모두 조회
    @Query("{\"bool\": {\"must\": [{\"term\": {\"authorId\": \"?0\"}}]}}")
    List<BookAuthorDocument> findBooksByAuthorId(Long authorId);

    // 책을 쓴 작가 모두 조회
    @Query("{\"bool\": {\"must\": [{\"term\": {\"bookId\": \"?0\"}}]}}")
    List<BookAuthorDocument> findAuthorsByBookId(Long bookId);

    @Query("""
        {
            "bool": {
                "must": [
                    { "term": { "bookId": "?0" }},
                    { "term": { "authorId": "?1" }}
                ]
            }
        }
    """)
    List<BookAuthorDocument> findByBookIdAndAuthorId(Long bookId, Long authorId);

}
