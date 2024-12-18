package com.nhnacademy.book.book.dto.request;


import com.nhnacademy.book.book.entity.BookAuthor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorRequestDto {
    @NotBlank(message = "Author name must not be empty")
    private String authorName;

}

