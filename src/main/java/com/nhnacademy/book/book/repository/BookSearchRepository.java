package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookSearchRepository extends ElasticsearchRepository<Book, Long> {

    List<Book> findByBookTitleContaining(String keyword);

}
