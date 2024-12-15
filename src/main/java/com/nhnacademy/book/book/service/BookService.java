package com.nhnacademy.book.book.service;

import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.dto.request.BookSearchRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    // 도서 검색 기능
    List<BookResponseDto> searchBooks(BookSearchRequestDto searchRequest);

    // 도서 상세 조회 기능
    BookDetailResponseDto getBookDetail(Long sellingId, Long bookId);

    // 도서 좋아요 기능
    void likeBook(Long bookId, BookLikeRequestDto likeRequest);

    // 도서 이미지 업로드 기능
    void uploadBookImage(Long bookId, MultipartFile file);

    // 도서 태그 지정 기능
    void updateBookTags(Long bookId, BookTagRequestDto tagRequest);

    // 도서 등록 기능 (관리자)
    void registerBook(BookRegisterDto bookRegisterDto);

    // 도서 카테고리 등록 기능 (관리자)
    void registerCategory(BookCategoryRegisterDto categoryRequest);

    // 도서 삭제 기능 (관리자)
    void deleteBook(Long bookId);

    // 도서 수정 기능 (관리자)
    void updateBook(Long bookId, BookRegisterDto bookUpdateRequest);
}
