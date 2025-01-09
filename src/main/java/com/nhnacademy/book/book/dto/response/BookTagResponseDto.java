package com.nhnacademy.book.book.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class BookTagResponseDto {
    private Long bookTagId;
    private Long tagId;
    private String tagName;
    private Long bookId;
    private String bookName;


    public BookTagResponseDto(Long bookTagId, Long tagId, String tagName, Long bookId, String bookName) {
        this.bookTagId = bookTagId;
        this.tagId = tagId;
        this.tagName = tagName;
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public BookTagResponseDto(Long tagId, String tagName, Long bookId, String bookTitle) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.bookId = bookId;
        this.bookName = bookTitle;
    }
}
