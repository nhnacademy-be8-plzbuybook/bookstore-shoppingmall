package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.entity.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategorySearchRepository extends ElasticsearchRepository<Category, Long> {
    Optional<Category> findByCategoryName(String categoryName);

    Optional<Category> findByCategoryNameAndParentCategory(String categoryName, Category parentCategory);

    List<Category> findByParentCategory(Category parentCategory);


}
