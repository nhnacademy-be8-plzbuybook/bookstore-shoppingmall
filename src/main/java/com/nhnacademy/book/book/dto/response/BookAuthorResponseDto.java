package com.nhnacademy.book.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorResponseDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long authorId;
    private String authorName;
}
