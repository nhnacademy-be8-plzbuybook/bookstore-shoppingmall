package com.nhnacademy.book.book.service.Impl;


import com.nhnacademy.book.book.dto.request.BookCategoryRequestDto;
import com.nhnacademy.book.book.dto.response.BookCategoryResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookCategory;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.BookCategoryRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BookCategoryServiceTest {

    @InjectMocks
    private BookCategoryService bookCategoryService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BookService bookService;



    @Mock
    private BookCategoryRepository bookCategoryRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private Publisher publisher;
    private Pageable pageable;

    List<Book> books = new ArrayList<>();
    List<Category> categories = new ArrayList<>();

    Book book1;
    Book book2;


    Category category;
    Category parentCategory;
    Category childCategory;

    @BeforeEach
    public void setUp() {
        publisher = new Publisher("Test Publisher");
        publisher.setPublisherId(1L);

        book1 = new Book(
                publisher,
                "Find Book Title1",
                "Book Index1",
                "Book Description1",
                LocalDate.of(2023, 12, 15),
                new BigDecimal("15.99"),
                "1234567890123453"
        );
        book1.setBookId(1L);
        book2 = new Book(
                publisher,
                "Find Book Title2",
                "Book Index2",
                "Book Description1",
                LocalDate.of(2023, 12, 15),
                new BigDecimal("15.99"),
                "1234567890123453"
        );
        book2.setBookId(2L);

        books.add(book1);
        books.add(book2);

        parentCategory = new Category();
        parentCategory.setCategoryId(1L);
        parentCategory.setCategoryDepth(0);
        parentCategory.setCategoryName("Parent Category");
        parentCategory.setParentCategory(null);

        // 자식 카테고리 설정
        childCategory = new Category();
        childCategory.setCategoryId(2L);
        childCategory.setCategoryDepth(1);
        childCategory.setCategoryName("Child Category");
        childCategory.setParentCategory(parentCategory);


        // 일반 카테고리 설정
        category = new Category();
        category.setCategoryId(3L);
        category.setCategoryDepth(0);
        category.setCategoryName("test1");
        category.setParentCategory(null);

        categories.add(category);
        categories.add(childCategory);
        categories.add(parentCategory);


        pageable = PageRequest.of(0, 2); // 첫 페이지, 한 페이지에 2개씩

    }

    @Test
    void createBookCategory() {
        BookCategoryRequestDto requestDto = new BookCategoryRequestDto();
        requestDto.setCategoryId(1L);
        requestDto.setBookId(1L);

        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategory(parentCategory);
        bookCategory.setBook(book1);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book1));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(parentCategory));
        when(bookCategoryRepository.save(any())).thenReturn(bookCategory);

        BookCategoryResponseDto a = bookCategoryService.createBookCategory(requestDto);

        verify(bookCategoryRepository, times(1)).save(any());
        assertEquals(book1.getBookTitle(), a.getBookTitle());
        assertEquals(parentCategory.getCategoryName(), a.getCategoryName());

    }

    @Test
    void createCategory_BookNotFound() {
        BookCategoryRequestDto requestDto = new BookCategoryRequestDto();
        requestDto.setCategoryId(1L);
        requestDto.setBookId(1L);

        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategory(parentCategory);
        bookCategory.setBook(book1);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            bookCategoryService.createBookCategory(requestDto);
        });

    }

    @Test
    void createCategory_CategoryNotFound() {
        BookCategoryRequestDto requestDto = new BookCategoryRequestDto();
        requestDto.setCategoryId(1L);
        requestDto.setBookId(1L);

        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategory(parentCategory);
        bookCategory.setBook(book1);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book1));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            bookCategoryService.createBookCategory(requestDto);
        });

    }




    @Test
    void findCategoriesByBookId(){

        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book1));
        when(bookCategoryRepository.findCategoriesByBookId(anyLong())).thenReturn(categories);

        List<CategoryResponseDto> categoryResponseDtos = bookCategoryService.findCategoriesByBookId(1L);

        verify(bookCategoryRepository, times(1)).findCategoriesByBookId(anyLong());
        assertEquals(categoryResponseDtos.size(), categories.size());

        for(CategoryResponseDto categoryResponseDto : categoryResponseDtos){
            log.info("{}",String.valueOf(categoryResponseDto.getCategoryId()));
            log.info("{}",categoryResponseDto.getCategoryName());
        }
    }

    @Test
    void findCategoriesByBookId_BookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> {
            bookCategoryService.findCategoriesByBookId(anyLong());
        });
    }





}
