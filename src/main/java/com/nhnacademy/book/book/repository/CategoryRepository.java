package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
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

    List<Category> findByParentCategory(Category parentCategory);

    List<Category> findByCategoryNameContaining(String keyword);

    @Query("SELECT c FROM Category c JOIN c.bookCategories bc WHERE bc.book.bookId = :bookId")
    List<Category> findCategoriesByBookId(@Param("bookId") Long bookId);

}
