package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.dto.request.BookSearchRequestDto;
import com.nhnacademy.book.book.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    // 도서 검색 기능
    @Override
    public List<BookResponseDto> searchBooks(BookSearchRequestDto searchRequest) {
        // 임시 데이터 반환 - 실제 DB 로직을 구현해야 합니다.
        List<BookResponseDto> books = new ArrayList<>();
        books.add(new BookResponseDto(1L, "Spring Boot Guide", "Author A", "IT", 20000, 18000));
        return books;
    }

    // 도서 상세 조회 기능
    @Override
    public BookDetailResponseDto getBookDetail(Long sellingId, Long bookId) {
        return new BookDetailResponseDto(bookId, "Spring Boot Guide", "목차...", "설명...", "Author A", "출판사 A", "2024-06-15", "1234567890123", 20000, 18000, 10, true);
    }

    // 도서 좋아요 기능
    @Override
    public void likeBook(Long bookId, BookLikeRequestDto likeRequest) {
        System.out.println("Book ID " + bookId + " 좋아요 처리됨.");
    }

    // 도서 이미지 업로드 기능
    @Override
    public void uploadBookImage(Long bookId, MultipartFile file) {
        System.out.println("Book ID " + bookId + " 이미지 업로드: " + file.getOriginalFilename());
    }

    // 도서 태그 지정 기능
    @Override
    public void updateBookTags(Long bookId, BookTagRequestDto tagRequest) {
        System.out.println("Book ID " + bookId + " 태그 지정: " + tagRequest.getTags());
    }

    // 도서 등록 기능 (관리자)
    @Override
    public void registerBook(BookRegisterDto bookRegisterDto) {
        System.out.println("도서 등록: " + bookRegisterDto.getTitle());
    }

    // 도서 카테고리 등록 기능 (관리자)
    @Override
    public void registerCategory(BookCategoryRegisterDto categoryRequest) {
        System.out.println("카테고리 등록: " + categoryRequest.getCategoryName());
    }

    // 도서 삭제 기능 (관리자)
    @Override
    public void deleteBook(Long bookId) {
        System.out.println("Book ID " + bookId + " 삭제됨.");
    }

    // 도서 수정 기능 (관리자)
    @Override
    public void updateBook(Long bookId, BookRegisterDto bookUpdateRequest) {
        System.out.println("Book ID " + bookId + " 수정됨: " + bookUpdateRequest.getTitle());
    }
}
