package com.nhnacademy.book.book.dto.response.aladin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 리스트 API의 응답 전체 구조를 나타냄 (메타데이터와 도서 목록 포함).
 */
@Getter
@Setter
public class AladinBookListResponse {
    @JsonProperty("version")
    private String version;

    @JsonProperty("title")
    private String title;

    @JsonProperty("pubDate")
    private String pubDate;

    @JsonProperty("totalResults")
    private int totalResults;

    @JsonProperty("startIndex")
    private int startIndex;

    @JsonProperty("itemsPerPage")
    private int itemsPerPage;

    @JsonProperty("query")
    private String query;

    @JsonProperty("searchCategoryName")
    private String searchCategoryName;

    @JsonProperty("item")  // JSON에서 "item" 키에 해당
    private List<AladinResponse> item;

    public List<AladinResponse> getBooks() {
        return item;
    }

    public void setBooks(List<AladinResponse> books) {
        this.item = books;
    }
}
