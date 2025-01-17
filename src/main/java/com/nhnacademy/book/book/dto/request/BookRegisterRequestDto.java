package com.nhnacademy.book.book.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@RequiredArgsConstructor
//@AllArgsConstructor
public class BookRegisterRequestDto {
    private Long bookId;
    private String bookTitle;
    private String bookIndex;
    private String bookDescription;
    private LocalDate bookPubDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal bookPriceStandard;
    private String bookIsbn13;
    private String publisher;       // 출판사
    private String imageUrl;
    private List<CategoryResponseDto> categories;        // 카테고리
    private List<String> authors; // 작가 정보

    public BookRegisterRequestDto(Long bookId, String bookTitle, String bookIndex,
                           String bookDescription, LocalDate bookPubDate,
                           BigDecimal bookPriceStandard, String bookIsbn13, String publisherName,
                           String imageUrl, List<CategoryResponseDto> categoryDtos,
                           List<String> authors) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookIndex = bookIndex;
        this.bookDescription = bookDescription;
        this.bookPubDate = bookPubDate;
        this.bookPriceStandard = bookPriceStandard;
        this.bookIsbn13 = bookIsbn13;
        this.publisher = publisherName; // 필드명이 publisherName이 아닌 publisher일 경우
        this.imageUrl = imageUrl;
        this.categories = categoryDtos; // categories로 변경
        this.authors = authors;
    }






}
