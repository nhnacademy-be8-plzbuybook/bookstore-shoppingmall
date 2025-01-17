package com.nhnacademy.book.booktest.controller;


import com.nhnacademy.book.book.controller.SearchBookController;
import com.nhnacademy.book.book.dto.response.BookInfoResponseDto;
import com.nhnacademy.book.book.service.Impl.BookSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchBookController.class)
public class SearchBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookSearchService bookSearchService;

    @BeforeEach
    void setUp() {
        // 초기 설정
    }

    @Test
    void testSearchBooks() throws Exception {
        String searchKeyword = "test";
        Pageable pageable = PageRequest.of(0, 3);
        BookInfoResponseDto bookInfoResponseDto = new BookInfoResponseDto();
        bookInfoResponseDto.setBookTitle("Test Book");
        Page<BookInfoResponseDto> books = new PageImpl<>(List.of(bookInfoResponseDto), pageable, 1);

        Mockito.when(bookSearchService.searchBooksByKeyword2(eq(searchKeyword), any(Pageable.class)))
                .thenReturn(books);

        mockMvc.perform(get("/api/search")
                        .param("searchKeyword", searchKeyword)
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookTitle").value("Test Book"));
    }
}
