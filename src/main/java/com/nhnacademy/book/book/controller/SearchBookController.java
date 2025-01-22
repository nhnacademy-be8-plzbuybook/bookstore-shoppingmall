package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.response.BookInfoResponseDto;
import com.nhnacademy.book.book.service.Impl.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchBookController {

    private final BookSearchService bookSearchService;

    @GetMapping("/api/search")
    public ResponseEntity<Page<BookInfoResponseDto>> searchBooks(
            @RequestParam String searchKeyword,
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, 3);
        return ResponseEntity.ok(bookSearchService.searchBooksByKeyword2(searchKeyword, pageable));
    }


    @GetMapping("/api/categories/{category-id}/books")
    public ResponseEntity<Page<BookInfoResponseDto>> searchBooksByCategory(@PathVariable(name="category-id") Long categoryId, @RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, 3);
        return ResponseEntity.ok(bookSearchService.findByExactCategoryName(categoryId, pageable));
    }

}
