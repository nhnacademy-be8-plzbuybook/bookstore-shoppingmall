package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.BookDocument;
import com.nhnacademy.book.book.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, Long> {

    BookDocument findByBookId(Long id);

}
