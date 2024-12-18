package com.nhnacademy.book.book.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Data
public class BookAuthorRequestDto {

    private Long authorId;  // Author ID
    private Long bookId;    // Book ID

}
