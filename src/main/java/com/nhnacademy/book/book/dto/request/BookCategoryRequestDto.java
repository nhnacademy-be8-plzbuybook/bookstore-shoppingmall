package com.nhnacademy.book.book.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BookCategoryRequestDto {
    private Long bookId;
    private Long categoryId;
}
