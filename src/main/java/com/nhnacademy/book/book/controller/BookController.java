package com.nhnacademy.book.book.controller;


import com.nhnacademy.book.book.dto.request.*;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.dto.response.SellingBookResponseDto;
import com.nhnacademy.book.book.service.Impl.BookAuthorService;
import com.nhnacademy.book.book.service.Impl.BookService;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    @Autowired
    private final BookService bookService;
    @Autowired
    private BookAuthorService bookAuthorService;
    @Autowired
    private SellingBookService sellingBookService;


    @GetMapping
    public ResponseEntity<List<BookDetailResponseDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // 도서 상세 조회 기능
    @GetMapping("/{bookId}")
    public ResponseEntity<BookDetailResponseDto> getBookDetail(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookDetail(bookId));
    }


    @GetMapping("/search/{bookId}")
    public ResponseEntity<BookDetailResponseDto> searchBooks(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookDetailFromElastic(bookId));
    }

    // 도서 등록 기능 (관리자)
    @PostMapping
    public ResponseEntity<Void> registerBook(@RequestBody BookRegisterDto bookRegisterDto) {
        bookService.registerBook(bookRegisterDto);
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


    //작가 아이디로 작가가 집필한 책 검색
    @GetMapping("/{authorId}")
    public ResponseEntity<List<BookResponseDto>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookAuthorService.findBooksByAuthorId(authorId));
    }

    @GetMapping("/selling/{authorId}")
    public ResponseEntity<List<SellingBookResponseDto>> findBooksByAuthorIdWithSellingInfo(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookAuthorService.findBooksByAuthorIdWithSellingInfo(authorId));
    }

    @GetMapping("/{authorId}/selling-books")
    public ResponseEntity<List<SellingBookResponseDto>> getBooksByAuthorWithSellingInfo(@PathVariable Long authorId) {
        List<SellingBookResponseDto> sellingBooks = bookAuthorService.findBooksByAuthorIdWithSellingInfo(authorId);
        return ResponseEntity.ok(sellingBooks);
    }

}
