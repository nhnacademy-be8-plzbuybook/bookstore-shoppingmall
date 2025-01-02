package com.nhnacademy.book.book.controller;


import com.nhnacademy.book.book.dto.request.*;
import com.nhnacademy.book.book.dto.response.*;
import com.nhnacademy.book.book.elastic.document.BookDocument;
import com.nhnacademy.book.book.elastic.repository.BookSearchRepository;
import com.nhnacademy.book.book.service.Impl.BookAuthorService;
import com.nhnacademy.book.book.service.Impl.BookSearchService;
import com.nhnacademy.book.book.service.Impl.BookService;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private BookSearchService bookSearchService;
    @Autowired
    private BookSearchRepository bookSearchRepository;

//
//    @GetMapping
//    public ResponseEntity<List<BookDetailResponseDto>> getAllBooks() {
//        return ResponseEntity.ok(bookService.getAllBooks());
//    }

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
    @GetMapping("/authors/{authorId}")
    public ResponseEntity<List<BookResponseDto>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookAuthorService.findBooksByAuthorId(authorId));
    }
//
//    //키워드로 검색(책 이름, 작가, 카테고리 등)
//    @GetMapping
//    public ResponseEntity<BookSearchPagedResponseDto> searchBooks(
//            @RequestParam String searchKeyword,
//            @RequestParam(defaultValue = "0") int page,         // 기본값으로 첫 번째 페이지
//            @RequestParam(defaultValue = "3") int size        // 기본값으로 한 페이지에 3개
//    ) {
//        // Pageable 객체 생성
//        Pageable pageable = PageRequest.of(page, size);
//
//        // BookSearchService를 통해 페이징된 데이터 가져오기
//        Page<BookSearchResponseDto> booksPage = bookSearchService.searchBooks(searchKeyword, pageable);
//
//        // 전체 책 개수를 가져오기 (검색된 전체 데이터 개수)
//        long totalElements = bookSearchService.totalElements(searchKeyword);
//
//        // 전체 페이지 수 계산
//        long totalPages = (long) Math.ceil((double) totalElements / size);
//
//        // BookSearchPagedResponseDto 생성 및 반환
//        BookSearchPagedResponseDto response = new BookSearchPagedResponseDto(
//                booksPage.getContent(),       // 현재 페이지의 데이터 리스트
//                booksPage.getNumber(),        // 현재 페이지 번호
//                (int) totalPages,            // 전체 페이지 수
//                totalElements                // 전체 데이터 수
//        );
//
//        return ResponseEntity.ok(response);
//    }


//    @GetMapping
//    public ResponseEntity<List<BookSearchResponseDto>> searchBooks(
//            @RequestParam String searchKeyword) {
//
//        return ResponseEntity.ok(bookSearchService.searchBooksByKeyword(searchKeyword));
//    }

    @GetMapping
    public ResponseEntity<Page<BookSearchResponseDto>> searchBooks(
            @RequestParam String searchKeyword,
            @RequestParam(defaultValue = "0") int page   // 기본값은 0 (첫 페이지)
            )  // 기본값은 10 (한 페이지당 10개)
    {
        Pageable pageable = PageRequest.of(page, 3);  // Pageable 객체 생성
        return ResponseEntity.ok(bookSearchService.searchBooksByKeyword(searchKeyword, pageable));
    }


}
