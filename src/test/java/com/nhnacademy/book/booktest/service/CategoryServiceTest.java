package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.dto.request.CategoryRegisterDto;
import com.nhnacademy.book.book.dto.request.ParentCategoryRequestDto;
import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
import com.nhnacademy.book.book.elastic.repository.CategorySearchRepository;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.exception.CategoryAlreadyExistsException;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.CategoryRepository;
import com.nhnacademy.book.book.service.Impl.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategorySearchRepository categorySearchRepository;

    private Category category;
    private Category parentCategory;
    private Category childCategory;
    private ParentCategoryRequestDto parentCategoryRequestDto;
    private CategoryRegisterDto categoryRegisterDto;

    @BeforeEach
    public void setUp() {
        // 부모 카테고리 설정
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

        // ParentCategoryRequestDto 설정
        parentCategoryRequestDto = new ParentCategoryRequestDto();
        parentCategoryRequestDto.setCategoryId(1L);

        categoryRegisterDto = new CategoryRegisterDto();
        categoryRegisterDto.setParentCategoryId(1L);
        categoryRegisterDto.setParentCategoryName("Parent Category");
        categoryRegisterDto.setNewCategoryName("New Category");



    }

    @Test
    void findCategoryById() {
        when(categoryRepository.findById(3L)).thenReturn(Optional.ofNullable(category));

        CategoryResponseDto foundCategory = categoryService.findCategoryById(3L);

        assertNotNull(foundCategory);
        assertEquals(3L, foundCategory.getCategoryId());
        assertEquals(0, foundCategory.getCategoryDepth());
        assertEquals("test1", foundCategory.getCategoryName());
    }

    @Test
    void findCategoryById_CategoryNotFoundException() {
        assertThrows(CategoryNotFoundException.class, () -> categoryService.findCategoryById(null));
    }

//    @Test
//    void findAllCategories() {
//        List<Category> categories = Arrays.asList(category, parentCategory, childCategory);
//        when(categoryRepository.findAll()).thenReturn(categories);
//
//        List<CategoryResponseDto> allCategoriesDTO = categoryService.findAllCategories();
//
//        assertNotNull(allCategoriesDTO);
//        assertEquals(3, allCategoriesDTO.size());
//    }


    @Test
    void findCategoryByName() {
        Mockito.when(categoryRepository.findByCategoryName("test1")).thenReturn(Optional.ofNullable(category));
        CategoryResponseDto foundCategory = categoryService.findCategoryByName("test1");
        assertNotNull(foundCategory);
        assertEquals(3L, foundCategory.getCategoryId());
        assertEquals(0, foundCategory.getCategoryDepth());
        assertEquals("test1", foundCategory.getCategoryName());
    }

    @Test
    void findCategoryByName_CategoryNotFoundException() {
        Mockito.when(categoryRepository.findByCategoryName("test1")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findCategoryByName("test1"));
    }

    @Test
    void findByParentCategory() {
        when(categoryRepository.findByParentCategory(parentCategory)).thenReturn(Arrays.asList(childCategory));
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(parentCategory));


        List<CategoryResponseDto> childCategoriesDTO = categoryService.findByParentCategory(parentCategoryRequestDto);
        assertNotNull(childCategoriesDTO);
        assertEquals(1, childCategoriesDTO.size());
    }

    @Test
    void findByParentCategory_NoChildren() {
        // given
        ParentCategoryRequestDto parentCategoryRequestDto = new ParentCategoryRequestDto();
        parentCategoryRequestDto.setCategoryId(1L);  // 부모 카테고리 ID 설정

        // 부모 카테고리 설정
        Category parentCategory = new Category("Parent Category", 0, null);  // 부모 카테고리 생성

        // when
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(parentCategory));  // 부모 카테고리 반환
        when(categoryRepository.findByParentCategory(any(Category.class))).thenReturn(Collections.emptyList());  // 자식 카테고리 없도록 설정

        // then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.findByParentCategory(parentCategoryRequestDto));  // 자식 카테고리가 없으면 예외 발생
    }

    @Test
    void findByParentCategory_parentCategoryDtoIdNull() {
        parentCategoryRequestDto.setCategoryId(null);
        assertThrows(CategoryNotFoundException.class, () -> categoryService.findByParentCategory(parentCategoryRequestDto));
    }

    @Test
    void findByParentCategory_parentCategoryDtoNull() {

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findByParentCategory(null));
    }


    @Test
    void saveCategory_ShouldSaveNewCategory() {
        when(categoryRepository.findByCategoryName(categoryRegisterDto.getNewCategoryName())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryRepository.findByCategoryId(anyLong())).thenReturn(Optional.of(category));

        CategoryResponseDto savedCategory = categoryService.saveCategory(categoryRegisterDto);

        assertNotNull(savedCategory);
        assertEquals("test1", savedCategory.getCategoryName());
        assertEquals(0, savedCategory.getCategoryDepth());
    }

    @Test
    void saveCategory_ShouldThrowCategoryAlreadyExistsException_WhenCategoryNameExists() {
        when(categoryRepository.findByCategoryName("New Category")).thenReturn(Optional.of(category));

        assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.saveCategory(categoryRegisterDto));
    }

    @Test
    void saveCategory_WithParentCategory() {
        String categoryName = "New Category";
        ParentCategoryRequestDto parentCategoryRequestDto = new ParentCategoryRequestDto();
        parentCategoryRequestDto.setCategoryId(1L);
        // 부모 카테고리 설정
        Category parentCategory = new Category("Parent Category", 0, null);
        parentCategory.setCategoryId(1L);

        Category newCategory = new Category(categoryName, parentCategory.getCategoryDepth() + 1, parentCategory);

        when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.findByCategoryId(anyLong())).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        CategoryResponseDto response = categoryService.saveCategory(categoryRegisterDto);

        // 부모 카테고리에서 자식 카테고리가 추가되었는지 확인
        verify(categoryRepository).save(any(Category.class));
        assertNotNull(response);
        assertEquals(categoryName, response.getCategoryName());
        assertEquals(parentCategory.getCategoryDepth() + 1, response.getCategoryDepth());
    }



    @Test
    void deleteCategoryById() {
        Mockito.when(categoryRepository.existsById(2L)).thenReturn(true);
        Mockito.when(categorySearchRepository.existsById(2L)).thenReturn(true);


        categoryService.deleteCategoryById(2L);

        assertFalse(categoryRepository.findById(2L).isPresent());
        assertFalse(categorySearchRepository.findById(2L).isPresent());

    }

    @Test
    void deleteCategoryById_CategoryNotFoundException() {
        Mockito.when(categoryRepository.existsById(2L)).thenReturn(false);
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategoryById(2L));
    }
}
