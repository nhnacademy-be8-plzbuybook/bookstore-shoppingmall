package com.nhnacademy.book.book.dto.request;

import com.nhnacademy.book.book.entity.Category;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class ParentCategoryRequestDto {

    private Long categoryId;
    private String categoryName;

}
