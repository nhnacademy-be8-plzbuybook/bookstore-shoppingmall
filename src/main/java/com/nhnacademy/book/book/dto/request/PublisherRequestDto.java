package com.nhnacademy.book.book.dto.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class PublisherRequestDto {
    private Long publisherId;
    private String publisherName;
}
