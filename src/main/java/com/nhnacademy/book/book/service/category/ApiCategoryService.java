package com.nhnacademy.book.book.service.category;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.exception.CategoryAlreadyExistsException;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCategoryService {


    @Autowired
    private  CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;


    /**
     * 카테고리 경로를 > 기준으로 나눠서 저장
     * @param fullPath 예시: "국내도서>예술/대중문화>음악>악보/작곡>피아노 및 건반악기 악보"
     */
    @Transactional
    public void processCategoryPath(String fullPath) {
        final String[] categoryParts = fullPath.split(">"); // 카테고리 경로를 > 기준으로 나눔
        Category parentCategory = null; // 초기 부모 카테고리 설정

        log.debug("처리할 경로: {}", fullPath);

        for (int depth = 0; depth < categoryParts.length; depth++) {
            final String categoryName = categoryParts[depth].trim(); // 각 카테고리 이름

            log.debug("현재 카테고리 이름: {}, 깊이: {}", categoryName, depth + 1);

            // 부모와 이름을 기준으로 카테고리 찾기
            final Category currentParent = parentCategory; // 람다식 내 사용되는 변수는 final 또는 effectively final 이어야 함

            Category category = categoryRepository.findByCategoryNameAndParentCategory(categoryName, currentParent)
                    .orElse(null);

            if (category == null) {
                log.debug("새로운 카테고리 생성: {}, 깊이: {}", categoryName, depth + 1);
                // 새 카테고리 생성
                category = new Category();
                category.setCategoryName(categoryName);
                category.setCategoryDepth(depth + 1); // depth 설정
                category.setParentCategory(currentParent); // 부모 카테고리 설정

                categoryRepository.save(category); // 저장
                log.debug("저장된 카테고리: {}, ID: {}", category.getCategoryName(), category.getCategoryId());
            } else {
                log.debug("이미 존재하는 카테고리: {}, ID: {}", category.getCategoryName(), category.getCategoryId());
            }

            parentCategory = category; // 현재 카테고리를 부모로 설정
        }
    }
    /**
     * 카테고리 경로를 저장합니다.
     * @param book Book 엔티티
     * @param categoryPath 카테고리 전체 경로 (예: "국내도서>예술/대중문화>음악>악보/작곡>피아노 및 건반악기 악보")
     */
    public void saveBookCategory(Book book, String categoryPath) {
        if (categoryPath != null && !categoryPath.isEmpty()) {
            // 카테고리 경로를 > 기준으로 나눠서 저장
            processCategoryPath(categoryPath);

            // 최하위 카테고리 (마지막 깊이) 찾기
            String[] categoryParts = categoryPath.split(">");
            String lastCategoryName = categoryParts[categoryParts.length - 1].trim();

            // 최하위 카테고리를 찾거나 새로 생성
            Category category = categoryRepository
                    .findByCategoryNameAndParentCategory(lastCategoryName, null)
                    .orElseGet(() -> {
                        log.debug("최하위 카테고리 생성: {}", lastCategoryName);
                        Category newCategory = new Category();
                        newCategory.setCategoryName(lastCategoryName);
                        newCategory.setCategoryDepth(categoryParts.length);
                        newCategory.setParentCategory(null); // 부모 없음
                        return categoryRepository.save(newCategory);
                    });

            // 도서에 카테고리 연결
            book.addCategory(category);
            bookRepository.save(book); // save 호출 확인

            log.debug("도서에 연결된 최하위 카테고리: {}", lastCategoryName);
        }
    }
}

