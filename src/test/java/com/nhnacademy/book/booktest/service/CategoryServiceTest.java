package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.repository.CategoryRepository;
import com.nhnacademy.book.book.service.Impl.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;


    private Category category;
    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setCategoryId(1L);
        category.setCategoryDepth(0);
        category.setCategoryName("test1");
        category.setParentCategory(null);


    }

    @Test
    void findCategoryById(){

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
        Category category = categoryService.findCategoryById(1L);
        assertNotNull(category);
        assertEquals(category.getCategoryId(), 1L);
        assertEquals(category.getCategoryDepth(), 0);
        assertEquals(category.getCategoryName(), "test1");
    }
    @Test
    void findCategoryById_CategoryNotFoundException(){
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
    }
}
