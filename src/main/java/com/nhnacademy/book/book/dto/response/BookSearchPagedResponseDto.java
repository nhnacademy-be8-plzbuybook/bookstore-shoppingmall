package com.nhnacademy.book.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class BookSearchPagedResponseDto {
    private List<BookSearchResponseDto> content = new ArrayList<>(); // 현재 페이지의 책 목록
    private int currentPage;                  // 현재 페이지 번호
    private int totalPages;                   // 전체 페이지 수
    private long totalElements;               // 전체 데이터 수
}