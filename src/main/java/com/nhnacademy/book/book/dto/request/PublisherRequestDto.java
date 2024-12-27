package com.nhnacademy.book.book.dto.request;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
public class PublisherRequestDto {
    private Long publisherId;
    private String publisherName;
}
