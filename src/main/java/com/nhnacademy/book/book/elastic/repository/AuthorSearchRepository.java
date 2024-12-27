package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.AuthorDocument;
import com.nhnacademy.book.book.entity.Author;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorSearchRepository extends ElasticsearchRepository<AuthorDocument, Long> {

    AuthorDocument findByAuthorName(String authorName);


    void deleteByAuthorId(Long authorId);

}
