package com.nhnacademy.book.book.dto.response;

import com.nhnacademy.book.book.entity.Publisher;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
public class BookResponseDto {
    private Long bookId;
    private String bookTitle;
    private BigDecimal standardBookPrice; // 판매가
    private String bookIsbn13;
    private LocalDate bookPubDate; // 출판일
    private String publisher; // 출판사


    public BookResponseDto() {

    }
}
