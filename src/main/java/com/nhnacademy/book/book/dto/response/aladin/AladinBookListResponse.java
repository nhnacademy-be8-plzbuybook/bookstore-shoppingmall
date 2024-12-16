package com.nhnacademy.book.book.dto.response.aladin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AladinBookListResponse {
    private String version;
    private String title;
    private String pubDate;
    private int totalResults;
    private int startIndex;
    private int itemsPerPage;
    private String query;
    private String searchCategoryName;
    private List<AladinResponse> item; // API에서 item 배열

    public List<AladinResponse> getBooks() {
        return item;
    }

    public void setBooks(List<AladinResponse> books) {
        this.item = books;
    }
}
