package com.nhnacademy.book.book.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class BookTagRequestDto {
    private List<String> tags;
}
