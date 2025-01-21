package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryId(Long categoryId);

    Optional<Category> findByCategoryName(String categoryName);

    Optional<Category> findByCategoryNameAndParentCategory(String categoryName, Category parentCategory);


//    List<Category> findByCategoryNameContaining(String keyword);

    @Query("SELECT c FROM Category c JOIN c.bookCategories bc WHERE bc.book.bookId = :bookId")
    List<Category> findCategoriesByBookId(@Param("bookId") Long bookId);

    Page<Category> findByCategoryNameContaining(String keyword, Pageable pageable);

    // 특정 카테고리의 자식 카테고리 목록 조회
    List<Category> findByParentCategory(Category parentCategory);

    // 특정 부모 카테고리 ID를 기준으로 자식 카테고리 목록 조회
    @Query("SELECT c FROM Category c WHERE c.parentCategory.categoryId = :parentId")
    List<Category> findByParentCategoryId(@Param("parentId") Long parentId);

    @Query("SELECT c FROM Category c WHERE c.parentCategory.categoryId = 1 AND c.categoryDepth = 2")
    List<Category> findChildCategoriesDepth2();




    @Modifying
    @Query(value = """
    WITH RECURSIVE CategoryHierarchy AS (
        SELECT category_id
        FROM category
        WHERE category_id = :categoryId
        UNION ALL
        SELECT c.category_id
        FROM category c
        INNER JOIN CategoryHierarchy ch ON c.category_parent_id = ch.category_id
    )
    DELETE FROM category
    WHERE category_id IN (SELECT category_id FROM CategoryHierarchy)
""", nativeQuery = true)
    void deleteCategoryAndChildren(@Param("categoryId") Long categoryId);

    List<Category> findByParentCategoryCategoryId(Long parentId);
}
