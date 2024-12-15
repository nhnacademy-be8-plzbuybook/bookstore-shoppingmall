package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CategoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        // 카테고리 엔티티 객체 초기화
        category = new Category();
        category.setCategoryName("Test Category");
        category.setCategoryDepth(1);
    }

    @Test
    void testSaveCategory() {
        // Given: 카테고리 객체를 데이터베이스에 저장
        Category savedCategory = categoryRepository.save(category);

        // When: 카테고리가 저장된 후, ID가 할당된 상태인지 확인
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getCategoryId()).isNotNull();

        // Then: 저장된 카테고리의 이름과 깊이가 예상대로 설정되었는지 확인
        assertThat(savedCategory.getCategoryName()).isEqualTo("Test Category");
        assertThat(savedCategory.getCategoryDepth()).isEqualTo(1);
    }

    @Test
    void testFindCategoryById() {
        // Given: 카테고리 객체 저장
        Category savedCategory = categoryRepository.save(category);

        // When: ID로 카테고리 조회
        Category foundCategory = categoryRepository.findById(savedCategory.getCategoryId()).orElse(null);

        // Then: 카테고리가 null이 아니고, 예상한 카테고리 값이 반환되는지 확인
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getCategoryName()).isEqualTo("Test Category");
        assertThat(foundCategory.getCategoryDepth()).isEqualTo(1);
    }

    @Test
    void testUpdateCategory() {
        // Given: 카테고리 객체 저장
        Category savedCategory = categoryRepository.save(category);

        // When: 카테고리 이름을 수정하고 저장
        savedCategory.setCategoryName("Updated Category");
        Category updatedCategory = categoryRepository.save(savedCategory);

        // Then: 수정된 카테고리 값이 반환되는지 확인
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getCategoryName()).isEqualTo("Updated Category");
    }

    @Test
    void testDeleteCategory() {
        // Given: 카테고리 객체 저장
        Category savedCategory = categoryRepository.save(category);

        // When: 카테고리 삭제
        categoryRepository.deleteById(savedCategory.getCategoryId());

        // Then: 카테고리가 삭제되었는지 확인
        assertThat(categoryRepository.findById(savedCategory.getCategoryId()).isPresent()).isFalse();
    }
}

