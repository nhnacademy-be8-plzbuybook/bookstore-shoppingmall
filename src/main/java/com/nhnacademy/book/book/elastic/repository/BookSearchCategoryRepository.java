package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.BookCategoryDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSearchCategoryRepository extends ElasticsearchRepository<BookCategoryDocument, Long> {

    @Query("{\"bool\": {\"must\": [{\"term\": {\"category_id\": ?0}}]}}")
    List<BookCategoryDocument> findBookIdsByCategoryId(Long categoryId);
}
