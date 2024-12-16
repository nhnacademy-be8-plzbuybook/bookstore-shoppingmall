package com.nhnacademy.book.book.service.Impl;


import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookCategory;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.BookCategoryRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookCategoryService {

    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public BookCategoryService(BookCategoryRepository bookCategoryRepository, BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    public BookCategory createBookCategory(Book book, Category category) {

        if(Objects.isNull(book)){
            throw new BookNotFoundException("Book not found");
        } else if(Objects.isNull(category)){
            throw new CategoryNotFoundException("Category not found");
        }
        BookCategory bookCategory = new BookCategory();
        bookCategory.setBook(book);
        bookCategory.setCategory(category);
        return bookCategoryRepository.save(bookCategory);

    }

    public List<Book> findBooksByCategory (Category category) {
        if(categoryRepository.findById(category.getCategoryId()).isEmpty()){
            throw new CategoryNotFoundException("Category not found");
        }
        List<Book> books = bookCategoryRepository.findBooksByCategory(category);
        if(books.isEmpty()){
            throw new BookNotFoundException("Books not found");
        }

        return bookCategoryRepository.findBooksByCategory(category);
    }

    public List<Category> findCategoriesByBookId(Long bookId) {
        if(bookRepository.existsById(bookId)){
            throw new BookNotFoundException("Book not found");
        }


        List<Category> categories = bookCategoryRepository.findCategoriesByBookId(bookId);

        if(categories.isEmpty()){
            throw new CategoryNotFoundException("Categories not found");
        }
        return categories;
    }

    public List<Book> findBooksByCategoryId(Long categoryId) {
        List<Book> books = bookCategoryRepository.findBooksByCategoryId(categoryId);
        if(books.isEmpty()){
            throw new BookNotFoundException("Books not found");
        }
        return books;
    }
}
