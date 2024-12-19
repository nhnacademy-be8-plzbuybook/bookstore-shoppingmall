package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.repository.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
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
        assertNotNull(savedCategory);
        assertEquals("잡지", savedCategory.getCategoryName());
        assertEquals(1, savedCategory.getCategoryDepth());
    }

    @Test
    void testFindCategoryByName() {
        // When
        Optional<Category> foundCategory = categoryRepository.findByCategoryName("도서");

        // Then
        assertTrue(foundCategory.isPresent());
        assertEquals("도서", foundCategory.get().getCategoryName());
    }

    @Test
    void testFindChildCategories() {
        // When
        Optional<Category> foundParentCategory = categoryRepository.findByCategoryName("도서");

        // Then
        assertTrue(foundParentCategory.isPresent());
        Category parent = foundParentCategory.get();
        assertEquals(2, parent.getChildrenCategory().size());
        assertTrue(parent.getChildrenCategory().stream()
                .anyMatch(child -> child.getCategoryName().equals("소설")));
        assertTrue(parent.getChildrenCategory().stream()
                .anyMatch(child -> child.getCategoryName().equals("만화")));
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
        assertFalse(deletedCategory.isPresent());
    }

    @Test
    void testUpdateCategory() {
        // Given
        Category categoryToUpdate = categoryRepository.findByCategoryName("도서").orElseThrow();

        // When
        categoryToUpdate.setCategoryName("전자책");
        Category updatedCategory = categoryRepository.save(categoryToUpdate);

        // Then
        assertEquals("전자책", updatedCategory.getCategoryName());
    }

    @Test
    void findByParentCategoryTest() {
        for (Category category : categoryRepository.findByParentCategory(parentCategory)) {
            log.info(category.getCategoryName());
        }

        List<Category> categoryList = categoryRepository.findByParentCategory(parentCategory);
        assertNotNull(categoryList);
        assertEquals(2, categoryList.size());
    }
}
