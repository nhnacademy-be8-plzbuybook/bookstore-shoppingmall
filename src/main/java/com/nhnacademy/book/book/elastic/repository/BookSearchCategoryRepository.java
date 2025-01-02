package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.BookCategoryDocument;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookCategory;
import com.nhnacademy.book.book.entity.Category;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSearchCategoryRepository extends ElasticsearchRepository<BookCategoryDocument, Long> {

    @Query("{\"query\": {\"term\": {\"category_id\": ?0}}}")
    List<BookCategoryDocument> findBookIdsByCategoryId(Long categoryId);
}
