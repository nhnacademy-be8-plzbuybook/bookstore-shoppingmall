package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.ParentCategoryRequestDto;
import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
import com.nhnacademy.book.book.dto.response.CategorySimpleResponseDto;
import com.nhnacademy.book.book.elastic.repository.CategorySearchRepository;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.exception.CategoryAlreadyExistsException;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategorySearchRepository categorySearchRepository;

    public CategoryService(CategoryRepository categoryRepository, CategorySearchRepository categorySearchRepository) {
        this.categoryRepository = categoryRepository;
        this.categorySearchRepository = categorySearchRepository;
    }

    public CategoryResponseDto findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));

        return convertToDto(category);
    }

    public List<CategoryResponseDto> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("Category list is empty");
        }

        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDto> findByParentCategory(ParentCategoryRequestDto parentCategoryDto) {
        if (parentCategoryDto == null || parentCategoryDto.getCategoryId() == null) {
            throw new CategoryNotFoundException("ParentCategoryDto or CategoryId is null");
        }

        // Parent 카테고리 조회
        Category parentCategory = categoryRepository.findById(parentCategoryDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("ParentCategory not found with ID: " + parentCategoryDto.getCategoryId()));

        // 자식 카테고리 조회
        List<Category> childrenCategories = categoryRepository.findByParentCategory(parentCategory);

        if (childrenCategories.isEmpty()) {
            throw new CategoryNotFoundException("No children categories found for parent: " + parentCategory.getCategoryName());
        }

        // Category -> CategoryResponseDto 변환
        return childrenCategories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public CategoryResponseDto findCategoryByName(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with name: " + categoryName));

        return convertToDto(category);
    }

    public CategoryResponseDto saveCategory(String categoryName, ParentCategoryRequestDto parentCategoryRequestDto) {
        // 이미 존재하는 카테고리인지 확인
        if (categoryRepository.findByCategoryName(categoryName).isPresent()) {
            throw new CategoryAlreadyExistsException("Category already exists with name: " + categoryName);
        }

        Category parentCategory = null;
        int categoryDepth;

        if (parentCategoryRequestDto != null && categoryRepository.existsById(parentCategoryRequestDto.getCategoryId())) {
            parentCategory = categoryRepository.findById(parentCategoryRequestDto.getCategoryId()).get();
            categoryDepth = parentCategory.getCategoryDepth() + 1;
        } else {
            categoryDepth = 0;
        }

        Category newCategory = new Category(categoryName, categoryDepth, parentCategory);

        // 부모 카테고리가 있으면 자식 추가
        if (parentCategory != null) {
            parentCategory.addChildCategory(newCategory);
        }

        Category savedCategory = categoryRepository.save(newCategory);
        return convertToDto(savedCategory);
    }


    public void deleteCategoryById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId) || !categorySearchRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category not found with ID: " + categoryId);
        }
        categorySearchRepository.deleteById(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    // Category 엔티티를 CategoryResponseDto로 변환하는 메서드
    private CategoryResponseDto convertToDto(Category category) {
        List<CategoryResponseDto> childCategories = category.getChildrenCategory().stream()
                .map(child -> new CategoryResponseDto(
                        child.getCategoryId(),
                        child.getCategoryName(),
                        child.getCategoryDepth(),
                        child.getParentCategory() != null ? child.getParentCategory().getCategoryId() : null,
                        new ArrayList<>()
                ))
                .collect(Collectors.toList());

        return new CategoryResponseDto(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getCategoryDepth(),
                category.getParentCategory() != null ? category.getParentCategory().getCategoryId() : null,
                childCategories
        );
    }


    public List<CategorySimpleResponseDto> searchCategoriesByKeyword(String keyword) {
        return categoryRepository.findByCategoryNameContaining(keyword)
                .stream()
                .map(category -> new CategorySimpleResponseDto(category.getCategoryId(), category.getCategoryName()))
                .collect(Collectors.toList());
    }
}
