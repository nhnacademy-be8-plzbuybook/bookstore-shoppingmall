package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.BookDocument;
import com.nhnacademy.book.book.elastic.document.BookInfoDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookInfoRepository extends ElasticsearchRepository<BookInfoDocument, Long> {

    @Query("{\"bool\": {\"should\": [" +
            "{\"match\": {\"book_title\": {\"query\": \"?0\", \"operator\": \"or\", \"boost\": 3.0}}}, " +
            "{\"match\": {\"author_names\": {\"query\": \"?0\", \"operator\": \"or\", \"boost\": 1.5}}}, " +
            "{\"match\": {\"category_name\": {\"query\": \"?0\", \"operator\": \"or\"}}}, " +
            "{\"match\": {\"tag_name\": {\"query\": \"?0\", \"operator\": \"or\"}}} ], " +
            "\"minimum_should_match\": 1}}")
    List<BookInfoDocument> searchBooksByKeyword(String keyword);

    void deleteByBookId(Long bookId);

    void deleteBySellingBookId(Long sellingBookId);


    @Query("{\"match\": {\"category_name\": {\"query\": \"?0\", \"operator\": \"or\"}}}")
    List<BookInfoDocument> findByExactCategoryName(String categoryName);






}
