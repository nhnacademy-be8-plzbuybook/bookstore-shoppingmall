package com.nhnacademy.book.booktest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.controller.CategoryController;
import com.nhnacademy.book.book.dto.request.CategoryRegisterDto;
import com.nhnacademy.book.book.dto.response.CategorySimpleResponseDto;
import com.nhnacademy.book.book.service.Impl.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }
//
//    @Test
//    void testGetSimpleCategoriesWithoutKeyword() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//        CategorySimpleResponseDto categoryResponse = new CategorySimpleResponseDto();
//        categoryResponse.setCategoryName("Test Category");
//        Page<CategorySimpleResponseDto> categories = new PageImpl<>(List.of(categoryResponse), pageable, 1);
//
//        Mockito.when(categoryService.findAllCategories(pageable)).thenReturn(categories);
//
//        mockMvc.perform(get("/api/categories")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].categoryName").value("Test Category"));
//    }
//
//    @Test
//    void testGetSimpleCategoriesWithKeyword() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//        CategorySimpleResponseDto categoryResponse = new CategorySimpleResponseDto();
//        categoryResponse.setCategoryName("Keyword Match Category");
//        Page<CategorySimpleResponseDto> categories = new PageImpl<>(List.of(categoryResponse), pageable, 1);
//
//        Mockito.when(categoryService.searchCategoriesByKeyword(eq("keyword"), eq(pageable))).thenReturn(categories);
//
//        mockMvc.perform(get("/api/categories")
//                        .param("keyword", "keyword")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].categoryName").value("Keyword Match Category"));
//    }
//
//    @Test
//    void testDeleteCategory() throws Exception {
//        Long categoryId = 1L;
//
//        mockMvc.perform(delete("/api/categories/{categoryId}", categoryId))
//                .andExpect(status().isNoContent());
//
//        Mockito.verify(categoryService).deleteCategory(categoryId);
//    }
//
//    @Test
//    void testSaveCategory() throws Exception {
//        CategoryRegisterDto requestDto = new CategoryRegisterDto();
//        requestDto.setNewCategoryName("New Category");
//
//        mockMvc.perform(post("/api/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isCreated());
//
//        Mockito.verify(categoryService).saveCategory(any(CategoryRegisterDto.class));
//    }
}
