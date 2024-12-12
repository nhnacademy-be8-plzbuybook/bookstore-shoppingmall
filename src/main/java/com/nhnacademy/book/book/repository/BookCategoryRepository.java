package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookCategory;
import com.nhnacademy.book.book.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    // 카테고리에 속한 책 검색
    @Query("SELECT bc.book FROM BookCategory bc WHERE bc.category = :category")
    List<Book> findBooksByCategory(Category category);


    // 카테고리 및 하위 카테고리에 속한 책 검색
    @Query("SELECT bc.book FROM BookCategory bc WHERE bc.category = :category OR bc.category.parentCategory = :category")
    List<Book> findBooksByCategoryIncludingSubCategories(Category category);

}
