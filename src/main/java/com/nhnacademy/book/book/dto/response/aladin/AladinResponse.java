package com.nhnacademy.book.book.dto.response.aladin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


/**
 * 도서 하나의 세부 정보를 나타냄.
 */
@Getter
@Setter
public class AladinResponse {
    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private String author;

    @JsonProperty("pubDate")
    private String pubDate;

    @JsonProperty("isbn13")
    private String isbn13;

    @JsonProperty("publisher")
    private String publisher;

    @JsonProperty("priceStandard")
    private int priceStandard;

    @JsonProperty("cover")
    private String cover;

    @JsonProperty("description")
    private String description;

    @JsonProperty("categoryName")
    private String categoryName; // 카테고리 이름

}
