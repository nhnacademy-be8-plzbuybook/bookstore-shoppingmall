package com.nhnacademy.book.book.service.category;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ApiCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ApiCategoryService apiCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("도서에 카테고리를 저장하는 테스트")
    @Test
    void saveBookCategory_ShouldSaveBookWithCategory() {
        // Given
        Book mockBook = new Book();
        String categoryPath = "국내도서>문학>소설>한국소설";
        String[] categoryParts = categoryPath.split(">");

        Category parentCategory = null;

        for (int depth = 0; depth < categoryParts.length; depth++) {
            String categoryName = categoryParts[depth].trim();

            Category mockCategory = new Category();
            mockCategory.setCategoryName(categoryName);
            mockCategory.setCategoryDepth(depth + 1);
            mockCategory.setParentCategory(parentCategory);

            // Mock findByCategoryNameAndParentCategory
            when(categoryRepository.findByCategoryNameAndParentCategory(eq(categoryName), eq(parentCategory)))
                    .thenReturn(Optional.of(mockCategory));

            parentCategory = mockCategory;
        }

        String lastCategoryName = categoryParts[categoryParts.length - 1].trim();
        Category lastCategory = new Category();
        lastCategory.setCategoryName(lastCategoryName);
        lastCategory.setCategoryDepth(categoryParts.length);

        // Mock 최하위 카테고리 조회
        when(categoryRepository.findByCategoryNameAndParentCategory(eq(lastCategoryName), eq(null)))
                .thenReturn(Optional.of(lastCategory));

        when(bookRepository.save(any(Book.class))).thenReturn(mockBook);

        // When
        apiCategoryService.saveBookCategory(mockBook, categoryPath);

        // Then
        verify(categoryRepository, times(categoryParts.length + 1)) // +1 for the final category check
                .findByCategoryNameAndParentCategory(anyString(), any());
        verify(bookRepository, times(1)).save(mockBook);
    }

    @DisplayName("카테고리 경로를 처리하고 저장하는 테스트")
    @Test
    void processCategoryPath_ShouldSaveCategoriesCorrectly() {
        // Given
        String categoryPath = "국내도서>문학>소설>한국소설";
        String[] categoryParts = categoryPath.split(">");
        Category parentCategory = null;

        for (int depth = 0; depth < categoryParts.length; depth++) {
            String categoryName = categoryParts[depth].trim();

            // Mock findByCategoryNameAndParentCategory
            when(categoryRepository.findByCategoryNameAndParentCategory(eq(categoryName), eq(parentCategory)))
                    .thenReturn(Optional.empty());

            // Mock save method
            Category mockCategory = new Category();
            mockCategory.setCategoryName(categoryName);
            mockCategory.setCategoryDepth(depth + 1);
            mockCategory.setParentCategory(parentCategory);

            when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

            // Update parentCategory for the next iteration
            parentCategory = mockCategory;
        }

        // When
        apiCategoryService.processCategoryPath(categoryPath);

        // Then
        verify(categoryRepository, times(categoryParts.length))
                .findByCategoryNameAndParentCategory(anyString(), any());
        verify(categoryRepository, times(categoryParts.length)).save(any(Category.class));
    }


}
