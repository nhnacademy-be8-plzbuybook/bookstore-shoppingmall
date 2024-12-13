package com.nhnacademy.book.book.dto;

import com.nhnacademy.book.book.entity.Publisher;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class AladinResponse {
    private Publisher publisher;
    private String bookTitle;
    private String bookIndex;
    private String bookDescription;
    private LocalDate bookPubDate;
    private BigDecimal bookPriceStandard;
    private String bookIsbn;
    private String bookIsbn13;
    private String category;

}
