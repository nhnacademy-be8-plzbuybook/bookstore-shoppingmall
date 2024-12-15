package com.nhnacademy.book.book.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
public class BookRegisterDto {
    private String bookTitle;
    private String bookIndex;
    private String bookDescription;
    private LocalDate bookPubDate;
    private BigDecimal bookPriceStandard;
    private String bookIsbn;
    private String bookIsbn13;
    private Long publisherId;
}
