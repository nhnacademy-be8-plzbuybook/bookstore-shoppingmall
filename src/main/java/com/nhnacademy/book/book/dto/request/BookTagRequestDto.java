package com.nhnacademy.book.book.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookTagRequestDto {
    private String bookTitle;
    private String isbn;
    private LocalDate publishDate;
    private String bookIndex;
    private BigDecimal regularPrice;
    private int stock;
    private boolean isPackable;
    private String publisher;
    private String description;
    private String category;
    private String imageUrl;
}
