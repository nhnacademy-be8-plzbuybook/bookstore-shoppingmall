package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.PublisherDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherSearchRepository extends ElasticsearchRepository<PublisherDocument, Long> {

    PublisherDocument findByPublisherId(Long Id);
}
