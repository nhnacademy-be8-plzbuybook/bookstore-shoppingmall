package com.nhnacademy.book.booktest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.controller.AuthorController;
import com.nhnacademy.book.book.dto.request.AuthorRequestDto;
import com.nhnacademy.book.book.dto.response.AuthorResponseDto;
import com.nhnacademy.book.book.elastic.repository.AuthorSearchRepository;
import com.nhnacademy.book.book.service.Impl.AuthorService;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorSearchRepository authorSearchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthorRequestDto authorRequestDto;
    private AuthorResponseDto authorResponseDto;

    @BeforeEach
    void setUp() {
        authorRequestDto = new AuthorRequestDto("Test Author");
        authorResponseDto = new AuthorResponseDto(1L, "Test Author");
    }

    @Test
    void createAuthor() throws Exception {
        // doNothing() 대신 사용
        Mockito.doAnswer(invocation -> null).when(authorService).createAuthor(any(AuthorRequestDto.class));

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequestDto)))
                .andDo(print()) // 로그 출력
                .andExpect(status().isOk());

        Mockito.verify(authorService, Mockito.times(1)).createAuthor(any(AuthorRequestDto.class));
    }


    @Test
    void getAllAuthors() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AuthorResponseDto> authors = new PageImpl<>(Collections.singletonList(authorResponseDto), pageable, 1);

        Mockito.when(authorService.findAll(pageable)).thenReturn(authors);

        mockMvc.perform(get("/api/admin/authors")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].authorId").value(authorResponseDto.getAuthorId()))
                .andExpect(jsonPath("$.content[0].authorName").value(authorResponseDto.getAuthorName()));

        Mockito.verify(authorService, Mockito.times(1)).findAll(pageable);
    }

    @Test
    void searchAuthorsByKeyword() throws Exception {
        String keyword = "Test";
        Pageable pageable = PageRequest.of(0, 10);
        Page<AuthorResponseDto> authors = new PageImpl<>(Collections.singletonList(authorResponseDto), pageable, 1);

        Mockito.when(authorService.searchAuthorsByKeyword(keyword, pageable)).thenReturn(authors);

        mockMvc.perform(get("/api/admin/authors")
                        .param("keyword", keyword)
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].authorId").value(authorResponseDto.getAuthorId()))
                .andExpect(jsonPath("$.content[0].authorName").value(authorResponseDto.getAuthorName()));

        Mockito.verify(authorService, Mockito.times(1)).searchAuthorsByKeyword(keyword, pageable);
    }

    @Test
    void getAuthor() throws Exception {
        Mockito.when(authorService.getAuthorById(anyLong())).thenReturn(authorResponseDto);

        mockMvc.perform(get("/api/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorResponseDto)));

        Mockito.verify(authorService, Mockito.times(1)).getAuthorById(anyLong());
    }

    @Test
    void deleteAuthor() throws Exception {
        Mockito.doNothing().when(authorService).deleteAuthorById(anyLong());
        Mockito.doNothing().when(authorSearchRepository).deleteById(anyLong());

        mockMvc.perform(delete("/api/authors/{authorId}", 1L))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(authorService, Mockito.times(1)).deleteAuthorById(1L);
        Mockito.verify(authorSearchRepository, Mockito.times(1)).deleteById(1L);
    }
}
