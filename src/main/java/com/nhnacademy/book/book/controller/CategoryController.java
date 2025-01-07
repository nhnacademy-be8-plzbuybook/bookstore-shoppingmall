package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.request.CategoryRegisterDto;
import com.nhnacademy.book.book.dto.request.ParentCategoryRequestDto;
import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
import com.nhnacademy.book.book.dto.response.CategorySimpleResponseDto;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.service.Impl.CategoryService;
import jakarta.ws.rs.PUT;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<List<CategorySimpleResponseDto>> getSimpleCategories(
            @RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            List<CategorySimpleResponseDto> categories = categoryService.searchCategoriesByKeyword(keyword);
            return ResponseEntity.ok(categories);
        }
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> saveCategory(@RequestBody CategoryRegisterDto categoryRegisterDto) {

        categoryService.saveCategory(categoryRegisterDto);
        return ResponseEntity.ok().build();
    }




}
