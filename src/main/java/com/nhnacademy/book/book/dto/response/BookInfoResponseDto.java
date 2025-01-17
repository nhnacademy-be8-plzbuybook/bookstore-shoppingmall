package com.nhnacademy.book.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@Getter
@Setter
public class BookInfoResponseDto {
    private Long bookId; // 책 ID
    private String bookTitle; // 책 제목
    private String publisherName; // 출판사 이름
    private String categoryName; // 카테고리 이름
    private String authorName; // 작가 이름
    private BigDecimal sellingBookPrice; // 판매 가격
    private BigDecimal bookPriceStandard; // 표준 가격
    private String imageUrl; // 이미지 URL
    private Long sellingBookId;


    public BookInfoResponseDto() {

    }
}
