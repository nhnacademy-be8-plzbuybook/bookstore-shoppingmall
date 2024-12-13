package com.nhnacademy.book.book.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AladinBookListResponse {
    private List<AladinResponse> books;

    public List<AladinResponse> getBooks() {
        return books;
    }

    public void setBooks(List<AladinResponse> books) {
        this.books = books;
    }
}

