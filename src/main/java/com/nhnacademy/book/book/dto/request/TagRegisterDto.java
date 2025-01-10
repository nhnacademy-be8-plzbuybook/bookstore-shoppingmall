package com.nhnacademy.book.book.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@RequiredArgsConstructor
@Setter
public class TagRegisterDto {

    private String tagName;
}
