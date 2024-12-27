package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.response.CategorySimpleResponseDto;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.service.Impl.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/search")
    public ResponseEntity<List<CategorySimpleResponseDto>> searchCategories(@RequestParam String keyword) {
        List<CategorySimpleResponseDto> categories = categoryService.searchCategoriesByKeyword(keyword);
        return ResponseEntity.ok(categories);
    }
}
