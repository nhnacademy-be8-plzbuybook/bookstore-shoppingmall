package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.exception.CategoryAlreadyExistsException;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.CategoryRepository;
import com.nhnacademy.book.book.service.Impl.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;
    private Category parentCategory;
    private Category childCategory;
    Category newCategory;

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
        categoryService.saveCategory("childCategory",parentCategory);

        // 일반 카테고리 설정
        category = new Category();
        category.setCategoryId(3L);
        category.setCategoryDepth(0);
        category.setCategoryName("test1");
        category.setParentCategory(null);

        newCategory = new Category("New Category", 1, parentCategory);

    }

    @Test
    void findCategoryById() {
        when(categoryRepository.findById(3L)).thenReturn(Optional.ofNullable(category));

        Category foundCategory = categoryService.findCategoryById(3L);

        assertNotNull(foundCategory);
        assertEquals(3L, foundCategory.getCategoryId());
        assertEquals(0, foundCategory.getCategoryDepth());
        assertEquals("test1", foundCategory.getCategoryName());
    }

    @Test
    void findCategoryById_CategoryNotFoundException() {
        assertThrows(CategoryNotFoundException.class, () -> categoryService.findCategoryById(null));
    }

    @Test
    void findAllCategories() {
        List<Category> categories = Arrays.asList(category, parentCategory, childCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> allCategories = categoryService.findAllCategories();


        assertNotNull(allCategories);
        assertEquals(3, allCategories.size());
        assertTrue(allCategories.contains(category));
        assertTrue(allCategories.contains(parentCategory));
        assertTrue(allCategories.contains(childCategory));
    }

    @Test
    void findAllCategories_CategoryNotFoundException() {
        List<Category> categories = Arrays.asList();
        when(categoryRepository.findAll()).thenReturn(categories);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findAllCategories());

    }

    @Test
    void findCategoryByName() {
        Mockito.when(categoryRepository.findByCategoryName("test1")).thenReturn(Optional.ofNullable(category));
        Category foundCategory = categoryService.findCategoryByName("test1");
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

        List<Category> childCategories = categoryService.findByParentCategory(parentCategory);

        assertNotNull(childCategories);
        assertEquals(1, childCategories.size());
        assertEquals("Child Category", childCategories.getFirst().getCategoryName());
        assertEquals(parentCategory, childCategories.getFirst().getParentCategory());
    }
    @Test
    void findByParentCategory_NoChildren() {
        categoryService.saveCategory("test",null);
        assertThrows(CategoryNotFoundException.class, () -> categoryService.findByParentCategory(category));
    }

    @Test
    void saveCategory_ShouldSaveNewCategory() {
        when(categoryRepository.findByCategoryName("New Category")).thenReturn(java.util.Optional.empty());
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(newCategory);

        // when: 카테고리 저장 호출
        Category savedCategory = categoryService.saveCategory("New Category", parentCategory);

        // then: 카테고리가 저장된 것을 검증
        assertNotNull(savedCategory);
        assertEquals("New Category", savedCategory.getCategoryName());
        assertEquals(1, savedCategory.getCategoryDepth());
    }

    @Test
    void saveCategory_ShouldThrowCategoryAlreadyExistsException_WhenCategoryNameExists() {
        when(categoryRepository.findByCategoryName("New Category")).thenReturn(java.util.Optional.of(newCategory));

        assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.saveCategory("New Category", parentCategory));

    }

    @Test
    void saveCategory_WhenParentCategoryIsNull() {
        Category category2 = new Category();
        category2.setCategoryId(4L);
        category2.setCategoryName("test2");
        category2.setParentCategory(null);
        category2.setCategoryDepth(0);

        when(categoryRepository.findByCategoryName("test2")).thenReturn(java.util.Optional.empty());
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category2);

        Category savedCategory = categoryService.saveCategory("test2", null);

        assertNotNull(savedCategory);
        assertEquals("test2", savedCategory.getCategoryName());
        assertEquals(0, savedCategory.getCategoryDepth());
    }

    @Test
    void deleteCategoryById() {

        Mockito.when(categoryRepository.existsById(2L)).thenReturn(true);

        categoryService.deleteCategoryById(2L);

        assertFalse(categoryRepository.findById(2L).isPresent());

    }

    @Test
    void deleteCategoryById_CategoryNotFoundException() {
        Mockito.when(categoryRepository.existsById(2L)).thenReturn(false);
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategoryById(2L));
    }
}
