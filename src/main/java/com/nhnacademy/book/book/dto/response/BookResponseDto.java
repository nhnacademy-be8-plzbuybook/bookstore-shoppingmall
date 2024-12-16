package com.nhnacademy.book.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BookResponseDto {
    private Long bookId;
    private String bookTitle;
    private BigDecimal bookPriceStandard;
    private String bookIsbn;
    private String bookIsbn13;
}
