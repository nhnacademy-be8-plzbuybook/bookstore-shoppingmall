package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.BookImageDocument;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookSearchImageRepository extends ElasticsearchRepository<BookImageDocument, Long> {
}
