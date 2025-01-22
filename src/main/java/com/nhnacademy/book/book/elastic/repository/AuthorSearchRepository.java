package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.AuthorDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorSearchRepository extends ElasticsearchRepository<AuthorDocument, Long> {

    AuthorDocument findByAuthorName(String authorName);


    void deleteByAuthorId(Long authorId);
    
    @Query("{\"match\": {\"author_name\": {\"query\": \"?0\", \"operator\": \"or\"}}}")
    List<AuthorDocument> findByAuthorNameContaining(String keyword);



}
