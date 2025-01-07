//package com.nhnacademy.book.booktest.controller;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.book.book.controller.CategoryController;
//import com.nhnacademy.book.book.dto.request.CategoryRegisterDto;
//import com.nhnacademy.book.book.service.Impl.CategoryService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(CategoryController.class)
//class CategoryControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CategoryService categoryService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testSaveCategory() throws Exception {
//        // Given
//        CategoryRegisterDto categoryRegisterDto = new CategoryRegisterDto();
//        categoryRegisterDto.setParentCategoryId(154L);
//        categoryRegisterDto.setParentCategoryName("국내도서");
//        categoryRegisterDto.setNewCategoryName("New Category44");
//
//        CategoryService categoryService = mock(CategoryService.class);
//
//        mockMvc.perform(post("/api/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(categoryRegisterDto)))
//                .andExpect(status().isOk());
//
//        verify(categoryService, times(1)).saveCategory(any(CategoryRegisterDto.class));
//    }
//}
