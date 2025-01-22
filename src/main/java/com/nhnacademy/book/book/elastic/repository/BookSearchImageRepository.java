package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.BookImageDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookSearchImageRepository extends ElasticsearchRepository<BookImageDocument, Long> {

    List<BookImageDocument> findByBookId(Long bookId);

}
