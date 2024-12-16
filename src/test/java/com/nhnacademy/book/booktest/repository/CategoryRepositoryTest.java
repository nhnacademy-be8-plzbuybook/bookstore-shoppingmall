package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.repository.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EntityManager entityManager;

    private Category parentCategory;
    private Category childCategory1;
    private Category childCategory2;

    @BeforeEach
    void setUp() {
        // 부모 카테고리 생성
        parentCategory = new Category("도서", 1, null);
        categoryRepository.save(parentCategory);

        // 자식 카테고리 생성
        childCategory1 = new Category("소설", 2, parentCategory);
        childCategory2 = new Category("만화", 2, parentCategory);

        parentCategory.addChildCategory(childCategory1);
        parentCategory.addChildCategory(childCategory2);

        categoryRepository.save(parentCategory);
    }

    @Test
    void testSaveCategory() {
        // Given
        Category category = new Category("잡지", 1, null);

        // When
        Category savedCategory = categoryRepository.save(category);

        // Then
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getCategoryName()).isEqualTo("잡지");
        assertThat(savedCategory.getCategoryDepth()).isEqualTo(1);
    }

    @Test
    void testFindCategoryByName() {
        // When
        Optional<Category> foundCategory = categoryRepository.findByCategoryName("도서");

        // Then
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getCategoryName()).isEqualTo("도서");
    }

    @Test
    void testFindChildCategories() {
        // When
        Optional<Category> foundParentCategory = categoryRepository.findByCategoryName("도서");

        // Then
        assertThat(foundParentCategory).isPresent();
        Category parent = foundParentCategory.get();
        assertThat(parent.getChildrenCategory()).hasSize(2);
        assertThat(parent.getChildrenCategory())
                .extracting(Category::getCategoryName)
                .containsExactlyInAnyOrder("소설", "만화");
    }

    @Test
    @Transactional
    void testDeleteCategory() {
        // Given
        Category category = categoryRepository.findByCategoryName("소설").orElseThrow();

        // 부모-자식 관계 해제
        if (category.getParentCategory() != null) {
            category.getParentCategory().getChildrenCategory().remove(category);
        }

        // When
        categoryRepository.delete(category);
        entityManager.flush();  // 변경 사항 강제로 반영

        // Then
        Optional<Category> deletedCategory = categoryRepository.findByCategoryName("소설");
        assertThat(deletedCategory).isNotPresent();
    }


    @Test
    void testUpdateCategory() {
        // Given
        Category categoryToUpdate = categoryRepository.findByCategoryName("도서").orElseThrow();

        // When
        categoryToUpdate.setCategoryName("전자책");
        Category updatedCategory = categoryRepository.save(categoryToUpdate);

        // Then
        assertThat(updatedCategory.getCategoryName()).isEqualTo("전자책");
    }
}
