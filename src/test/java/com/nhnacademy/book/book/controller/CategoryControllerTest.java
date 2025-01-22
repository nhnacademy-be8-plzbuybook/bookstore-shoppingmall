package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.request.CategoryRegisterDto;
import com.nhnacademy.book.book.dto.response.CategorySimpleResponseDto;
import com.nhnacademy.book.book.service.Impl.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CategoryController categoryController = new CategoryController(categoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void deleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    void saveCategory() throws Exception {
        // CategoryRegisterDto 객체 생성 후 필드 설정
        CategoryRegisterDto categoryRegisterDto = new CategoryRegisterDto();
        categoryRegisterDto.setParentCategoryId(1L);
        categoryRegisterDto.setNewCategoryName("Fiction");

        when(categoryService.saveCategory(any(CategoryRegisterDto.class))).thenReturn(null);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"parentCategoryId\":1, \"newCategoryName\":\"Fiction\"}")) // 요청 본문 수정
                .andExpect(status().isCreated());

        verify(categoryService, times(1)).saveCategory(any(CategoryRegisterDto.class));
    }


    @Test
    void getSimpleCategories() throws Exception {
        // Mock 데이터 생성
        Page<CategorySimpleResponseDto> categories = new PageImpl<>(
                List.of(new CategorySimpleResponseDto(1L, "Fiction")),
                PageRequest.of(0, 10),
                1
        );

        // Mock 동작 정의
        when(categoryService.findAllCategories(any(Pageable.class))).thenReturn(categories);

        // 테스트 수행
        mockMvc.perform(get("/api/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 상태 코드 확인
                .andExpect(jsonPath("$.content", hasSize(1))) // 응답 크기 확인
                .andExpect(jsonPath("$.content[0].categoryId").value(1L)) // 필드 값 확인
                .andExpect(jsonPath("$.content[0].categoryName").value("Fiction"));

        // Mock 검증
        verify(categoryService, times(1)).findAllCategories(any());
    }
}
