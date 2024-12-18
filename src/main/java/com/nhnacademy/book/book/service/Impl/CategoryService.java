package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.exception.CategoryAlreadyExistsException;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));
    }

    public List<Category> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("Category list is empty");
        }
        return categories;
    }

    public List<Category> findByParentCategory(Category parentCategory) {
        if (parentCategory.getChildrenCategory().isEmpty()) {
            throw new CategoryNotFoundException("No children categories found for parent: " + parentCategory.getCategoryName());
        }
        return categoryRepository.findByParentCategory(parentCategory);
    }

    public Category findCategoryByName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with name: " + categoryName));
    }

    public Category saveCategory(String categoryName, Category parentCategory) {
        // 이미 존재하는 카테고리인지 확인
        if (categoryRepository.findByCategoryName(categoryName).isPresent()) {
            throw new CategoryAlreadyExistsException("Category already exists with name: " + categoryName);
        }

        // 부모 카테고리가 null일 경우 categoryDepth를 0으로 설정
        int categoryDepth = (parentCategory == null) ? 0 : parentCategory.getCategoryDepth() + 1;

        // 새로운 카테고리 생성
        Category newCategory = new Category(categoryName, categoryDepth, parentCategory);

        // 부모 카테고리가 null이 아닌 경우에만 자식 카테고리 추가
        if (parentCategory != null) {
            parentCategory.addChildCategory(newCategory);  // 부모에 자식 추가
        }

        // 새로운 카테고리 저장
        return categoryRepository.save(newCategory);
    }



    public void deleteCategoryById(Long categoryId) {
        if(!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category not found with ID: " + categoryId);
        }

        categoryRepository.deleteById(categoryId);
    }



    //TODO 여기는 필요하면 추가
//    // 루트 카테고리부터 트리 구조 조회
//    public Category getCategoryTree(Long rootCategoryId) {
//        Category rootCategory = findCategoryById(rootCategoryId);
//        populateChildren(rootCategory);
//        return rootCategory;
//    }
//
//    // 재귀적으로 자식 카테고리 채우기
//    private void populateChildren(Category category) {
//        List<Category> children = findByParentCategory(category);
//        category.setChildrenCategory(children);
//        for (Category child : children) {
//            populateChildren(child);
//        }
//    }
}
