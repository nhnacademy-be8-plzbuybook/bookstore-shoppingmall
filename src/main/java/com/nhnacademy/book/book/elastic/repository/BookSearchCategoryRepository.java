package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookCategory;
import com.nhnacademy.book.book.entity.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSearchCategoryRepository extends ElasticsearchRepository<BookCategory, Long> {

    // 카테고리에 속한 책 검색
    @Query("SELECT bc.book FROM BookCategory bc WHERE bc.category = :category")
    List<Book> findBooksByCategory(Category category);

    // 책 ID로 관련된 카테고리 조회
    @Query("SELECT bc.category FROM BookCategory bc WHERE bc.book.bookId = :bookId")
    List<Category> findCategoriesByBookId(@Param("bookId") Long bookId);
    // 카테고리 ID로 관련된 책 조회
    @Query("SELECT bc.book FROM BookCategory bc WHERE bc.category.categoryId = :categoryId")
    List<Book> findBooksByCategoryId(@Param("categoryId") Long categoryId);




    // 카테고리 및 하위 카테고리에 속한 책 검색
    @Query("SELECT bc.book FROM BookCategory bc WHERE bc.category = :category OR bc.category.parentCategory = :category")
    List<Book> findBooksByCategoryIncludingSubCategories(Category category);

}
