package com.nhnacademy.book.book.controller;


import com.nhnacademy.book.book.dto.request.BookRegisterRequestDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.BookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.service.Impl.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


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
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword

            ) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.isEmpty()) {
            return ResponseEntity.ok(bookService.searchBooksByKeyword(keyword, pageable));
        } else {

        }
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
