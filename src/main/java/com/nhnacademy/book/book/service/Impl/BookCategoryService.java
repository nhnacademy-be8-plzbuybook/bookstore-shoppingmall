package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.BookCategoryRequestDto;
import com.nhnacademy.book.book.dto.response.BookCategoryResponseDto;
import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookCategory;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.BookCategoryRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookCategoryService {

    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public BookCategoryService(BookCategoryRepository bookCategoryRepository, BookRepository bookRepository, CategoryRepository categoryRepository, CategoryService categoryService) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    public BookCategoryResponseDto createBookCategory(BookCategoryRequestDto bookCategoryRequestDto) {
        Book book = bookRepository.findById(bookCategoryRequestDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        Category category = categoryRepository.findById(bookCategoryRequestDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        BookCategory bookCategory = new BookCategory(book, category);
        bookCategory = bookCategoryRepository.save(bookCategory);

        return new BookCategoryResponseDto(
                bookCategory.getId(),
                bookCategory.getBook().getBookId(),
                bookCategory.getBook().getBookTitle(),
                bookCategory.getCategory().getCategoryId(),
                bookCategory.getCategory().getCategoryName()
        );
    }

    public List<CategoryResponseDto> findCategoriesByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        List<Category> categories = bookCategoryRepository.findCategoriesByBookId(bookId);



        // Category 엔티티를 CategoryResponseDto로 변환하여 반환
        return categories.stream()
                .map(category -> {
                    // 자식 카테고리 목록 생성
                    List<CategoryResponseDto> childCategories = category.getChildrenCategory().stream()
                            .map(child -> new CategoryResponseDto(
                                    child.getCategoryId(),
                                    child.getCategoryName(),
                                    child.getCategoryDepth(),
                                    child.getParentCategory() != null ? child.getParentCategory().getCategoryId() : null,
                                    new ArrayList<>()  // 자식 카테고리의 자식들을 추가할 경우 여기에 추가
                            ))
                            .collect(Collectors.toList());

                    return new CategoryResponseDto(
                            category.getCategoryId(),
                            category.getCategoryName(),
                            category.getCategoryDepth(),
                            category.getParentCategory() != null ? category.getParentCategory().getCategoryId() : null,
                            childCategories
                    );
                })
                .collect(Collectors.toList());
    }



}
