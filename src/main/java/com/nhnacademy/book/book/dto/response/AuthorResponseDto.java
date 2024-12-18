package com.nhnacademy.book.book.dto.response;


import com.nhnacademy.book.book.entity.BookAuthor;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Getter
public class AuthorResponseDto {
    private Long authorId;
    private String authorName;

    public AuthorResponseDto(Long authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }
}
