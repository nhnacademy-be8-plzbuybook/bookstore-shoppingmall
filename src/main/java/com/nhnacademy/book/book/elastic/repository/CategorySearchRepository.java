package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.CategoryDocument;
import com.nhnacademy.book.book.entity.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategorySearchRepository extends ElasticsearchRepository<CategoryDocument, Long> {
    Optional<CategoryDocument> findByCategoryName(String categoryName);

    Optional<CategoryDocument> findByCategoryNameAndParentCategory(String categoryName, Category parentCategory);

    List<CategoryDocument> findByParentCategory(Category parentCategory);


}
