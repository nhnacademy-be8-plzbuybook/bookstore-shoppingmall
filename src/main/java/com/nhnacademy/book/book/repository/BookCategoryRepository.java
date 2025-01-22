package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookCategory;
import com.nhnacademy.book.book.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

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

    List<BookCategory> findByBook_BookIdIn(List<Long> longs);

//    @Query("SELECT bc.book FROM BookCategory bc " +
//            "WHERE bc.category.categoryId IN (" +
//            "  SELECT c.categoryId FROM Category c " +
//            "  WHERE c.categoryId = :parentId " +
//            "  OR c.parentCategory.categoryId = :parentId " +
//            "  OR c.categoryId IN (" +
//            "    SELECT child.categoryId FROM Category child WHERE child.parentCategory.categoryId = c.categoryId" +
//            "    OR child.categoryId IN (" +
//            "      SELECT grandChild.categoryId FROM Category grandChild WHERE grandChild.parentCategory.categoryId = child.categoryId" +
//            "    )" +
//            "  )" +
//            ")")
//    Page<Book> findBooksByParentCategory(@Param("parentId") Long parentId, Pageable pageable);


    @Query("SELECT bc.book FROM BookCategory bc " +
            "WHERE bc.category IN (" +
            "    SELECT c FROM Category c " +
            "    WHERE c.categoryId = :parentId " +
            "    OR c.parentCategory.categoryId = :parentId " + // 자식 카테고리까지
            "    OR c.parentCategory.categoryId IN (" + // 자식의 자식 카테고리까지
            "        SELECT c2.parentCategory.categoryId FROM Category c2 WHERE c2.parentCategory.categoryId = :parentId" +
            "    )" +
            ")")
    Page<Book> findBooksByParentCategory(@Param("parentId") Long parentId, Pageable pageable);

    @Query("SELECT bc.book FROM BookCategory bc " +
            "WHERE bc.category IN :categories")
    Page<Book> findBooksByCategories(@Param("categories") List<Category> categories, Pageable pageable);


}
