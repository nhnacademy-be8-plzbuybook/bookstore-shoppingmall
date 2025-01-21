package com.nhnacademy.book.book.controller;


import com.nhnacademy.book.book.dto.request.BookRegisterRequestDto;
import com.nhnacademy.book.book.dto.response.*;
import com.nhnacademy.book.book.elastic.repository.BookSearchRepository;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.service.Impl.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    @Autowired
    private BookSearchService bookSearchService;
    @Autowired
    private BookSearchRepository bookSearchRepository;

    private final BookCategoryService bookCategoryService;


    // 도서 등록 기능 (관리자)
    @PostMapping
    public ResponseEntity<Void> registerBook(
            @RequestBody @Valid BookRegisterRequestDto registerDto) {

        bookService.registerBook(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // 도서 상세 조회 기능
    @GetMapping("/{bookId}")
    public ResponseEntity<BookDetailResponseDto> getBookDetail(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookDetail(bookId));
    }


    //관리자 페이지
    @GetMapping
    public ResponseEntity<Page<BookRegisterDto>> adminGetBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.getBooks(pageable));
    }


    // 도서 삭제 기능 (관리자)
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //도서 수정 기능 (관리자) - 수정할때 값 불러오기
    @GetMapping("/update/{bookId}")
    public ResponseEntity<BookRegisterRequestDto> getBook(@PathVariable Long bookId) {
        BookRegisterRequestDto bookDetail = bookService.getBookUpdate(bookId); // DTO 생성
        return ResponseEntity.ok(bookDetail);
    }

    // 도서 수정 기능 (관리자)
    @PutMapping
    public ResponseEntity<Void> updateBook( @RequestBody BookRegisterRequestDto bookUpdateRequest) {
        bookService.updateBook(bookUpdateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/not-in-selling-books")
    public ResponseEntity<Page<BookResponseDto>> getBooksNotInSellingBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<BookResponseDto> books = bookService.findBooksNotInSellingBooks(pageable);
        return ResponseEntity.ok(books);
    }

}
