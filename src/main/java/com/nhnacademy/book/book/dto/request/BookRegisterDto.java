package com.nhnacademy.book.book.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private String bookIsbn13;
    private Long publisherId;
    private String imageUrl;


}
