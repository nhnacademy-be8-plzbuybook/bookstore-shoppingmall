package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.SellingBookDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SellingBookSearchRepository extends ElasticsearchRepository<SellingBookDocument, Long> {


}
