package com.nhnacademy.book.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookCategoryResponseDto {
    private Long id;               // BookCategory의 ID
    private Long bookId;           // Book의 ID
    private String bookTitle;      // Book의 제목
    private Long categoryId;       // Category의 ID
    private String categoryName;   // Category의 이름
}
