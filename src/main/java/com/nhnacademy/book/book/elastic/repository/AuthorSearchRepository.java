package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.AuthorDocument;
import com.nhnacademy.book.book.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorSearchRepository extends ElasticsearchRepository<AuthorDocument, Long> {

    AuthorDocument findByAuthorName(String authorName);


    void deleteByAuthorId(Long authorId);

//    @Query("{\"match\": {\"author_name\": {\"query\": \"?0\", \"operator\": \"and\"}}}")
//    List<AuthorDocument> findByAuthorNameContaining(String keyword);

    @Query("{\"match\": {\"author_name\": {\"query\": \"?0\", \"operator\": \"or\"}}}")
    List<AuthorDocument> findByAuthorNameContaining(String keyword);



}
