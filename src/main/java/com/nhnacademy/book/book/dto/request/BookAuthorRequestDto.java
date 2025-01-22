package com.nhnacademy.book.book.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class BookAuthorRequestDto {

    private Long authorId;  // Author ID
    private Long bookId;    // Book ID

}
