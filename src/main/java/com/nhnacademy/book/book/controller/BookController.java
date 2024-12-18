package com.nhnacademy.book.book.controller;


import com.nhnacademy.book.book.dto.request.*;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;


    // 도서 검색 기능
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> searchBooks(@ModelAttribute BookSearchRequestDto searchRequest) {
        return ResponseEntity.ok(bookService.searchBooks(searchRequest));
    }

    // 도서 상세 조회 기능
    @GetMapping("/{sellingId}/books/{bookId}")
    public ResponseEntity<BookDetailResponseDto> getBookDetail(@PathVariable Long sellingId, @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookDetail(sellingId, bookId));
    }

    // 도서 좋아요 기능
    @PostMapping("/{bookId}/like")
    public ResponseEntity<Void> likeBook(@PathVariable Long bookId, @RequestBody BookLikeRequestDto likeRequest) {
        bookService.likeBook(bookId, likeRequest);
        return ResponseEntity.ok().build();
    }

    // 도서 이미지 업로드 기능
    @PostMapping("/{bookId}/image")
    public ResponseEntity<Void> uploadBookImage(@PathVariable Long bookId, @RequestParam MultipartFile file) {
        bookService.uploadBookImage(bookId, file);
        return ResponseEntity.ok().build();
    }

    // 도서 태그 지정 기능
    @PutMapping("/{bookId}/tags")
    public ResponseEntity<Void> updateBookTags(@PathVariable Long bookId, @RequestBody BookTagRequestDto tagRequest) {
        bookService.updateBookTags(bookId, tagRequest);
        return ResponseEntity.ok().build();
    }

    // 도서 등록 기능 (관리자)
    @PostMapping
    public ResponseEntity<Void> registerBook(@RequestBody BookRegisterDto bookRegisterDto) {
        bookService.registerBook(bookRegisterDto);
        return ResponseEntity.ok().build();
    }

    // 도서 카테고리 등록 기능 (관리자)
    @PostMapping("/categories")
    public ResponseEntity<Void> registerCategory(@RequestBody BookCategoryRegisterDto categoryRequest) {
        bookService.registerCategory(categoryRequest);
        return ResponseEntity.ok().build();
    }

    // 도서 삭제 기능 (관리자)
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok().build();
    }

    // 도서 수정 기능 (관리자)
    @PutMapping("/{bookId}")
    public ResponseEntity<Void> updateBook(@PathVariable Long bookId, @RequestBody BookRegisterDto bookUpdateRequest) {
        bookService.updateBook(bookId, bookUpdateRequest);
        return ResponseEntity.ok().build();
    }

}
