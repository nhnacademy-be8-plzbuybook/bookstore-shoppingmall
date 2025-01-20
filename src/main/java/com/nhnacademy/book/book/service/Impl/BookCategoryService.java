package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.BookCategoryRequestDto;
import com.nhnacademy.book.book.dto.response.BookCategoryResponseDto;
import com.nhnacademy.book.book.dto.response.BookInfoResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
import com.nhnacademy.book.book.elastic.document.BookInfoDocument;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookCategory;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.BookCategoryRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public Page<BookResponseDto> getBooksByParentCategory(Long parentId, Pageable pageable) {
        // 모든 자식 카테고리들 조회
        List<Category> allCategories = categoryService.findAllChildCategories(parentId);

        // Set을 사용하여 중복 제거
        Set<BookResponseDto> bookResponseDtoSet = new HashSet<>();
        long totalElements = 0;  // 총 데이터 수를 저장할 변수

        // 각 카테고리 별로 책을 조회하여 결과를 합침
        for (Category category : allCategories) {
            // 각 카테고리의 책 조회
            Page<Book> books = bookCategoryRepository.findBooksByCategories(Collections.singletonList(category), pageable);

            // 결과 리스트에 책 데이터 추가 (중복 제거)
            books.getContent().forEach(book -> {
                BookResponseDto bookResponseDto = new BookResponseDto(
                        book.getBookId(),
                        book.getBookTitle(),
                        book.getBookPriceStandard(),
                        book.getBookIsbn13(),
                        book.getBookPubDate(),
                        book.getPublisher().getPublisherName()
                );
                bookResponseDtoSet.add(bookResponseDto);  // 중복 자동 제거
            });

            // 총 데이터 수를 합산
            totalElements = bookResponseDtoSet.size();
        }

        // Set에서 List로 변환하여 PageImpl 생성
        List<BookResponseDto> bookResponseDtos = new ArrayList<>(bookResponseDtoSet);

        return new PageImpl<>(bookResponseDtos, pageable, totalElements);
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
