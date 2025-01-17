package com.nhnacademy.book.booktest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.controller.BookController;
import com.nhnacademy.book.book.dto.request.BookRegisterRequestDto;
import com.nhnacademy.book.book.dto.response.*;
import com.nhnacademy.book.book.elastic.repository.BookSearchRepository;
import com.nhnacademy.book.book.service.Impl.BookAuthorService;
import com.nhnacademy.book.book.service.Impl.BookSearchService;
import com.nhnacademy.book.book.service.Impl.BookService;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookAuthorService bookAuthorService;

    @MockBean
    private SellingBookService sellingBookService;

    @MockBean
    private BookSearchRepository searchRepository;

    @MockBean
    private BookSearchService bookSearchService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterBook() throws Exception {
        BookRegisterRequestDto requestDto = new BookRegisterRequestDto();
        requestDto.setBookTitle("Test Book");

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        Mockito.verify(bookService).registerBook(any(BookRegisterRequestDto.class));
    }

    @Test
    void testGetBookDetail() throws Exception {
        Long bookId = 1L;
        BookDetailResponseDto responseDto = new BookDetailResponseDto();
        responseDto.setBookId(bookId);
        responseDto.setBookTitle("Test Book");

        Mockito.when(bookService.getBookDetail(bookId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/books/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.bookTitle").value("Test Book"));
    }

    @Test
    void testAdminGetBooks() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        BookRegisterDto bookRegisterDto = new BookRegisterDto();
        bookRegisterDto.setBookTitle("Test Book");
        Page<BookRegisterDto> books = new PageImpl<>(List.of(bookRegisterDto), pageable, 1);

        Mockito.when(bookService.getBooks(pageable)).thenReturn(books);

        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookTitle").value("Test Book"));
    }

    @Test
    void testDeleteBook() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/api/books/{bookId}", bookId))
                .andExpect(status().isNoContent());

        Mockito.verify(bookService).deleteBook(bookId);
    }

    @Test
    void testGetBookForUpdate() throws Exception {
        Long bookId = 1L;
        BookRegisterRequestDto responseDto = new BookRegisterRequestDto();
        responseDto.setBookTitle("Test Book");

        Mockito.when(bookService.getBookUpdate(bookId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/books/update/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookTitle").value("Test Book"));
    }

    @Test
    void testUpdateBook() throws Exception {
        BookRegisterRequestDto requestDto = new BookRegisterRequestDto();
        requestDto.setBookTitle("Updated Book");

        mockMvc.perform(put("/api/books/{bookId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNoContent());

        Mockito.verify(bookService).updateBook(any(BookRegisterRequestDto.class));
    }

    @Test
    void testGetBooksNotInSellingBooks() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        BookResponseDto bookResponseDto = new BookResponseDto();
        bookResponseDto.setBookTitle("Unsold Book");
        Page<BookResponseDto> books = new PageImpl<>(List.of(bookResponseDto), pageable, 1);

        Mockito.when(bookService.findBooksNotInSellingBooks(pageable)).thenReturn(books);

        mockMvc.perform(get("/api/books/not-in-selling-books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookTitle").value("Unsold Book"));
    }
}

