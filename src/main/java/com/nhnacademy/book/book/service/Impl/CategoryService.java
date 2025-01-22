package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.CategoryRegisterDto;
import com.nhnacademy.book.book.dto.request.ParentCategoryRequestDto;
import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
import com.nhnacademy.book.book.dto.response.CategorySimpleResponseDto;
import com.nhnacademy.book.book.elastic.repository.CategorySearchRepository;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.exception.CategoryAlreadyExistsException;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategorySearchRepository categorySearchRepository;
    private static final String CATEGORY_NOT_FOUND_MSG = "Category not found with ID: ";

    public CategoryResponseDto findCategoryById(Long id) {
        Category category = categoryRepository.findByCategoryId(id)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND_MSG + id));

        return convertToDto(category);
    }


    public List<CategoryResponseDto> findLeafCategories(Long parentCategoryId) {
        Category parentCategory = categoryRepository.findById(parentCategoryId)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND_MSG + parentCategoryId));

        // 리프 노드를 찾는 재귀 호출
        List<Category> leafCategories = findLeafCategoriesRecursive(parentCategory);

        if (leafCategories.isEmpty()) {
            throw new CategoryNotFoundException("No leaf categories found for parent: " + parentCategory.getCategoryName());
        }

        // Category -> CategoryResponseDto 변환
        return leafCategories.stream()
                .map(this::convertToDto)
                .toList();
    }

    private List<Category> findLeafCategoriesRecursive(Category category) {
        List<Category> children = categoryRepository.findByParentCategoryId(category.getCategoryId());

        // 자식이 없으면 리프 노드이므로 현재 카테고리를 반환
        if (children.isEmpty()) {
            return List.of(category);
        }

        // 자식이 있으면 자식 카테고리에 대해 재귀 호출
        return children.stream()
                .flatMap(child -> findLeafCategoriesRecursive(child).stream())
                .toList();
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
                .toList();
    }


    public CategoryResponseDto findCategoryByName(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with name: " + categoryName));

        return convertToDto(category);
    }

    public CategoryResponseDto saveCategory(CategoryRegisterDto categoryRegisterDto) {
        // 중복 카테고리 확인
        String categoryName = categoryRegisterDto.getNewCategoryName();
        if (categoryRepository.findByCategoryName(categoryRegisterDto.getNewCategoryName()).isPresent()) {
            throw new CategoryAlreadyExistsException("Category already exists with name: " + categoryName);
        }

        Category parentCategory = null;
        int categoryDepth;

        // 부모 카테고리 확인 및 깊이 계산
        if (categoryRegisterDto.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findByCategoryId(categoryRegisterDto.getParentCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Parent category not found, id: " + categoryRegisterDto.getParentCategoryId()));
            categoryDepth = parentCategory.getCategoryDepth() + 1;
        } else {
            categoryDepth = 0; // 루트 카테고리
        }

        // 새로운 카테고리 생성
        Category newCategory = new Category(categoryName, categoryDepth, parentCategory);

        // 부모 카테고리에 자식 추가
        if (parentCategory != null) {
            parentCategory.addChildCategory(newCategory);
        }

        // 카테고리 저장
        Category savedCategory = categoryRepository.save(newCategory);

        // DTO 변환 후 반환
        return convertToDto(savedCategory);
    }



    public void deleteCategoryById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId) || !categorySearchRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(CATEGORY_NOT_FOUND_MSG + categoryId);
        }
        categorySearchRepository.deleteById(categoryId);
        categoryRepository.deleteById(categoryId);
    }


    public Page<CategorySimpleResponseDto> searchCategoriesByKeyword(String keyword, Pageable pageable) {
        Page<Category> categories = categoryRepository.findByCategoryNameContaining(keyword, pageable);
        return categories.map(category -> new CategorySimpleResponseDto(category.getCategoryId(), category.getCategoryName()));
    }

    public Page<CategorySimpleResponseDto> findAllCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("Category list is empty");
        }
        return categories.map(category -> new CategorySimpleResponseDto(category.getCategoryId(), category.getCategoryName()));
    }

    public void deleteCategory(Long categoryId) {
        if(!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(CATEGORY_NOT_FOUND_MSG + categoryId);
        }

        categoryRepository.deleteCategoryAndChildren(categoryId);
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
                .toList();

        return new CategoryResponseDto(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getCategoryDepth(),
                category.getParentCategory() != null ? category.getParentCategory().getCategoryId() : null,
                childCategories
        );
    }

}
