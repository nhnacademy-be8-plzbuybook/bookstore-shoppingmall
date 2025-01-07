package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.request.CategoryRegisterDto;
import com.nhnacademy.book.book.dto.request.ParentCategoryRequestDto;
import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
import com.nhnacademy.book.book.dto.response.CategorySimpleResponseDto;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.service.Impl.CategoryService;
import jakarta.ws.rs.PUT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

//
//    @GetMapping
//    public ResponseEntity<List<CategorySimpleResponseDto>> getSimpleCategories(
//            @RequestParam(required = false) String keyword) {
//        if (keyword != null && !keyword.isEmpty()) {
//            List<CategorySimpleResponseDto> categories = categoryService.searchCategoriesByKeyword(keyword);
//            return ResponseEntity.ok(categories);
//        }
//        return ResponseEntity.ok(categoryService.findAllCategories());
//    }

    @GetMapping
    public ResponseEntity<Page<CategorySimpleResponseDto>> getSimpleCategories(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);  // 페이지 번호와 크기 설정

        Page<CategorySimpleResponseDto> categories;
        if (keyword != null && !keyword.isEmpty()) {
            categories = categoryService.searchCategoriesByKeyword(keyword, pageable);  // 키워드에 맞는 카테고리 검색
        } else {
            categories = categoryService.findAllCategories(pageable);  // 모든 카테고리 조회
        }

        return ResponseEntity.ok(categories);  // 페이징된 카테고리 반환
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
