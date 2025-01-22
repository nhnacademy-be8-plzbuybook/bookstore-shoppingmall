package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.response.BookInfoResponseDto;
import com.nhnacademy.book.book.elastic.document.BookInfoDocument;
import com.nhnacademy.book.book.elastic.repository.BookInfoRepository;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookSearchServiceTest {

    @Mock
    private BookInfoRepository bookInfoRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookSearchService bookSearchService;

    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        pageable = PageRequest.of(0, 2); // 첫 페이지, 한 페이지에 2개씩
    }
    @Test
    void searchBooksByKeyword2_shouldReturnPagedResults() {
        String keyword = "test";
        Pageable pageable = PageRequest.of(0, 2);

        BookInfoDocument doc1 = new BookInfoDocument(
                1L, "Book 1", "Publisher A", "Category X", "Author A",
                new BigDecimal("10000"), new BigDecimal("12000"), "url1", 101L
        );
        BookInfoDocument doc2 = new BookInfoDocument(
                2L, "Book 2", "Publisher B", "Category Y", "Author B",
                new BigDecimal("20000"), new BigDecimal("22000"), "url2", 102L
        );
        BookInfoDocument doc3 = new BookInfoDocument(
                3L, "Book 3", "Publisher C", "Category Z", "Author C",
                new BigDecimal("30000"), new BigDecimal("32000"), "url3", 103L
        );

        List<BookInfoDocument> documents = Arrays.asList(doc1, doc2, doc3);

        when(bookInfoRepository.searchBooksByKeyword(keyword)).thenReturn(documents);

        Page<BookInfoResponseDto> result = bookSearchService.searchBooksByKeyword2(keyword, pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent().get(0).getBookTitle()).isEqualTo("Book 1");
        assertThat(result.getContent().get(1).getBookTitle()).isEqualTo("Book 2");

        verify(bookInfoRepository, times(1)).searchBooksByKeyword(keyword);
    }

    @Test
    void findByExactCategoryName_shouldReturnPagedResults() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName("Category X");

        BookInfoDocument doc1 = new BookInfoDocument(
                1L, "Book 1", "Publisher A", "Category X", "Author A",
                new BigDecimal("10000"), new BigDecimal("12000"), "url1", 101L
        );
        BookInfoDocument doc2 = new BookInfoDocument(
                2L, "Book 2", "Publisher B", "Category X", "Author B",
                new BigDecimal("20000"), new BigDecimal("22000"), "url2", 102L
        );
        List<BookInfoDocument> documents = Arrays.asList(doc1, doc2);

        when(categoryRepository.findByCategoryId(categoryId)).thenReturn(Optional.of(category));
        when(bookInfoRepository.findByExactCategoryName("Category X")).thenReturn(documents);

        Page<BookInfoResponseDto> result = bookSearchService.findByExactCategoryName(categoryId, pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getBookTitle()).isEqualTo("Book 1");
        assertThat(result.getContent().get(1).getBookTitle()).isEqualTo("Book 2");

        verify(categoryRepository, times(1)).findByCategoryId(categoryId);
        verify(bookInfoRepository, times(1)).findByExactCategoryName("Category X");
    }

    @Test
    void findByExactCategoryName_shouldThrowCategoryNotFoundException_whenCategoryNotFound() {
        Long categoryId = 1L;

        when(categoryRepository.findByCategoryId(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookSearchService.findByExactCategoryName(categoryId, pageable))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("category Not Found");

        verify(categoryRepository, times(1)).findByCategoryId(categoryId);
        verifyNoInteractions(bookInfoRepository);
    }

    @Test
    void findByExactCategoryName_shouldReturnEmptyPage_whenNoBooksFound() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName("Category X");

        when(categoryRepository.findByCategoryId(categoryId)).thenReturn(Optional.of(category));
        when(bookInfoRepository.findByExactCategoryName("Category X")).thenReturn(Collections.emptyList());

        Page<BookInfoResponseDto> result = bookSearchService.findByExactCategoryName(categoryId, pageable);

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();

        verify(categoryRepository, times(1)).findByCategoryId(categoryId);
        verify(bookInfoRepository, times(1)).findByExactCategoryName("Category X");
    }

    @Test
    void findByExactCategoryName_shouldHandleEmptyPageGracefully() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName("Category X");

        BookInfoDocument doc1 = new BookInfoDocument(
                1L, "Book 1", "Publisher A", "Category X", "Author A",
                new BigDecimal("10000"), new BigDecimal("12000"), "url1", 101L
        );

        when(categoryRepository.findByCategoryId(categoryId)).thenReturn(Optional.of(category));
        when(bookInfoRepository.findByExactCategoryName("Category X")).thenReturn(List.of(doc1));

        Pageable invalidPageable = PageRequest.of(5, 2); // 데이터보다 큰 페이지 요청

        Page<BookInfoResponseDto> result = bookSearchService.findByExactCategoryName(categoryId, invalidPageable);

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(1); // 데이터는 하나 존재
        verify(categoryRepository, times(1)).findByCategoryId(categoryId);
        verify(bookInfoRepository, times(1)).findByExactCategoryName("Category X");
    }

}
