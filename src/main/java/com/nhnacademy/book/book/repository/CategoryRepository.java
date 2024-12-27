package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryName(String categoryName);

    Optional<Category> findByCategoryNameAndParentCategory(String categoryName, Category parentCategory);

    List<Category> findByParentCategory(Category parentCategory);


}
