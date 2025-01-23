package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.service.api.ApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AladinApiController.class)
class AladinApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiService apiService;

    @BeforeEach
    void setUp() {
        // 초기화 필요 시 추가 작업
    }

    @Test
    void syncBooksFromListApi() throws Exception {
        // ApiService의 메서드 호출에 대해 doNothing()으로 설정
        Mockito.doNothing().when(apiService).saveBooksFromListApi(anyString(), anyString(), anyInt(), anyInt());

        mockMvc.perform(post("/api/books/sync")
                        .param("queryType", "Bestseller")
                        .param("searchTarget", "Book")
                        .param("start", "1")
                        .param("maxResults", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(apiService, Mockito.times(1))
                .saveBooksFromListApi("Bestseller", "Book", 1, 10);
    }

    @Test
    void syncBooksByIsbns() throws Exception {
        // 실패 ISBN 목록을 반환하도록 Mock 설정
        List<String> failedIsbns = List.of("12345", "67890");
        Mockito.when(apiService.saveBooksByItemIds(anyList())).thenReturn(failedIsbns);

        mockMvc.perform(post("/api/books/sync/isbn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"11111\", \"22222\", \"12345\", \"67890\"]"))
                .andDo(print())
                .andExpect(status().isPartialContent())
                .andExpect(jsonPath("$.message").value("Some books could not be saved."))
                .andExpect(jsonPath("$.failed").isArray())
                .andExpect(jsonPath("$.failed").isNotEmpty())
                .andExpect(jsonPath("$.failed[0]").value("12345"))
                .andExpect(jsonPath("$.failed[1]").value("67890"));

        Mockito.verify(apiService, Mockito.times(1)).saveBooksByItemIds(anyList());
    }
}
