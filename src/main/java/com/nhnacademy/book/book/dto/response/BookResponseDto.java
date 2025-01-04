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

public class BookResponseDto {
    private Long bookId;
    private String bookTitle;
    private BigDecimal bookPriceStandard;
    private String bookIsbn13;

    public BookResponseDto() {

    }


}
