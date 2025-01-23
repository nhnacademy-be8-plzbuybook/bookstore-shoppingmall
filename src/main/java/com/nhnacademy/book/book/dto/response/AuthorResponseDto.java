package com.nhnacademy.book.book.dto.response;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
