package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.SellingBookBookDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SellingBookBookSearchRepository extends ElasticsearchRepository<SellingBookBookDocument, Long> {

    Page<SellingBookBookDocument> findByBookTitleContainingOrAuthorNameContainingOrCategoryNameContaining(
            String query, String query2, String query3, Pageable pageable
    );
}
