package com.nhnacademy.book.book.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.book.book.entity.SellingBook;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class BookRegisterDto {
    private String bookTitle;
    private String bookIndex;
    private String bookDescription;
    private LocalDate bookPubDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal bookPriceStandard;

    private int stock;              // 재고
    private boolean isPackable;     // 포장 가능 여부

    private String bookIsbn13;
    private String publisher;       // 출판사
    private String imageUrl;
    private List<String> category;        // 카테고리


}
