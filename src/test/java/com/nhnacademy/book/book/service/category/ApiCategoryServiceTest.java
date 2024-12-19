package com.nhnacademy.book.book.service.category;

import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class ApiCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ApiCategoryService apiCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("카테고리 경로를 >로 분리하여 저장 성공 여부를 확인")
    @Test
    void processCategoryPath() {
        // Given
        String categoryPath = "국내도서>문학>소설>한국소설";
        String[] categories = categoryPath.split(">");

        // Mock 행동 설정: findByCategoryNameAndParentCategory는 항상 Optional.empty()를 반환
        when(categoryRepository.findByCategoryNameAndParentCategory(anyString(), any()))
                .thenReturn(Optional.empty());

        // Mock 새로운 카테고리 생성 및 반환
        for (String categoryName : categories) {
            Category mockCategory = new Category();
            mockCategory.setCategoryName(categoryName);
            when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);
        }

        // When
        apiCategoryService.processCategoryPath(categoryPath);

        // Then
        verify(categoryRepository, times(categories.length)).save(any(Category.class));
        verify(categoryRepository, times(categories.length)).findByCategoryNameAndParentCategory(anyString(), any());
    }
}