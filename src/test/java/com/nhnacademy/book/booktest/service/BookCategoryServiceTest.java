package com.nhnacademy.book.booktest.service;


import com.nhnacademy.book.book.dto.request.BookCategoryRequestDto;
import com.nhnacademy.book.book.dto.response.BookCategoryResponseDto;
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
import com.nhnacademy.book.book.service.Impl.BookCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class BookCategoryServiceTest {

    @InjectMocks
    private BookCategoryService bookCategoryService;

    @Mock
    private BookCategoryRepository bookCategoryRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private Publisher publisher;

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

    }

    @Test
    void createBookCategory() {
        BookCategoryRequestDto requestDto = new BookCategoryRequestDto();
        requestDto.setCategoryId(1L);
        requestDto.setBookId(1L);

        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategory(parentCategory);
        bookCategory.setBook(book1);

        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book1));
        Mockito.when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(parentCategory));
        Mockito.when(bookCategoryRepository.save(any())).thenReturn(bookCategory);

        BookCategoryResponseDto a = bookCategoryService.createBookCategory(requestDto);

        Mockito.verify(bookCategoryRepository, Mockito.times(1)).save(any());
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

        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

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

        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book1));
        Mockito.when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            bookCategoryService.createBookCategory(requestDto);
        });

    }




    @Test
    void findCategoriesByBookId(){

        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book1));
        Mockito.when(bookCategoryRepository.findCategoriesByBookId(anyLong())).thenReturn(categories);

        List<CategoryResponseDto> categoryResponseDtos = bookCategoryService.findCategoriesByBookId(1L);

        Mockito.verify(bookCategoryRepository, Mockito.times(1)).findCategoriesByBookId(anyLong());
        assertEquals(categoryResponseDtos.size(), categories.size());

        for(CategoryResponseDto categoryResponseDto : categoryResponseDtos){
            log.info("{}",String.valueOf(categoryResponseDto.getCategoryId()));
            log.info("{}",categoryResponseDto.getCategoryName());
        }
    }

    @Test
    void findCategoriesByBookId_BookNotFound() {
        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> {
            bookCategoryService.findCategoriesByBookId(anyLong());
        });
    }




}
