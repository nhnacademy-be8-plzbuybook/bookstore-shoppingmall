package com.nhnacademy.book.book.dto.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ParentCategoryRequestDto {

    private Long categoryId;
    private String categoryName;

}
