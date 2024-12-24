package com.nhnacademy.book.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Getter
@Setter
public class BookDetailResponseDto {
    private Long bookId;
    private String bookTitle;
    private String bookIndex;
    private String bookDescription;
    private LocalDate bookPubDate;
    private BigDecimal bookPriceStandard;
    private String bookIsbn13;
    private String publisherName;
    private String imageUrl;


}

