package com.nhnacademy.book.booktest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.controller.BookController;
import com.nhnacademy.book.book.dto.request.BookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.BookInfoResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.elastic.repository.BookSearchRepository;
import com.nhnacademy.book.book.service.Impl.BookSearchService;
import com.nhnacademy.book.book.service.Impl.BookService;
import com.nhnacademy.book.book.service.Impl.BookAuthorService;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookSearchService bookSearchService;

    @MockBean
    private BookAuthorService bookAuthorService;

    @MockBean
    private SellingBookService sellingBookService;

    @MockBean
    private BookSearchRepository searchRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetBookDetail() throws Exception {
        Long bookId = 1L;
        BookDetailResponseDto bookDetailResponseDto = new BookDetailResponseDto(
                bookId, 101L, "Book Title", "Index", "This is a description",
                LocalDate.of(2020, 1, 1), new BigDecimal("10000.00"),
                new BigDecimal("9000.00"), 100, "978-3-16-148410-0",
                1L, "Publisher Name", "image_url", Collections.singletonList("Category1"),
                Collections.singletonList("Author1"), "Available", 50L
        );
        when(bookService.getBookDetail(bookId)).thenReturn(bookDetailResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.bookTitle").value("Book Title"))
                .andExpect(jsonPath("$.bookDescription").value("This is a description"))
                .andExpect(jsonPath("$.bookPubDate").value("2020-01-01"))
                .andExpect(jsonPath("$.sellingPrice").value(9000.00));
    }

    @Test
    public void testSearchBooks() throws Exception {
        Long bookId = 1L;
        BookDetailResponseDto bookDetailResponseDto = new BookDetailResponseDto(
                bookId, 101L, "Book Title", "Index", "This is a description",
                LocalDate.of(2020, 1, 1), new BigDecimal("10000.00"),
                new BigDecimal("9000.00"), 100, "978-3-16-148410-0",
                1L, "Publisher Name", "image_url", Collections.singletonList("Category1"),
                Collections.singletonList("Author1"), "Available", 50L
        );
        when(bookService.getBookDetailFromElastic(bookId)).thenReturn(bookDetailResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/search/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.bookTitle").value("Book Title"));
    }

    @Test
    public void testRegisterBook() throws Exception {
        BookRegisterDto bookRegisterDto = new BookRegisterDto(
                "Book Title", "Index1", "This is a book description", LocalDate.of(2020, 1, 1),
                new BigDecimal("10000"), "978-3-16-148410-0", 101L, "image_url"
        );

        String bookJson = objectMapper.writeValueAsString(bookRegisterDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookTitle").value("Book Title"))
                .andExpect(jsonPath("$.bookIndex").value("Index1"))
                .andExpect(jsonPath("$.bookDescription").value("This is a book description"))
                .andExpect(jsonPath("$.bookPubDate").value("2020-01-01"))
                .andExpect(jsonPath("$.bookPriceStandard").value(10000))
                .andExpect(jsonPath("$.bookIsbn13").value("978-3-16-148410-0"))
                .andExpect(jsonPath("$.publisherId").value(101))
                .andExpect(jsonPath("$.imageUrl").value("image_url"));

        verify(bookService, times(1)).registerBook(any(BookRegisterDto.class));
    }

    @Test
    public void testDeleteBook() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/{bookId}", bookId))
                .andExpect(status().isOk());
        verify(bookService, times(1)).deleteBook(bookId);
    }

    @Test
    public void testUpdateBook() throws Exception {
        Long bookId = 1L;
        String bookJson = "{ \"bookTitle\": \"Updated Book Title\", \"author\": \"Updated Author\", \"publisher\": \"Updated Publisher\", \"price\": 15000 }";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk());
        verify(bookService, times(1)).updateBook(eq(bookId), any());
    }

    @Test
    public void testGetBooksByAuthor() throws Exception {
        Long authorId = 1L;

        BookResponseDto bookResponseDto = new BookResponseDto(
                1L, "Book Title", new BigDecimal(10000), "978-3-16-148410-0"
        );
        List<BookResponseDto> books = Collections.singletonList(bookResponseDto);

        when(bookAuthorService.findBooksByAuthorId(authorId)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/authors/{authorId}", authorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookTitle").value("Book Title"))
                .andExpect(jsonPath("$[0].bookPriceStandard").value(10000))
                .andExpect(jsonPath("$[0].bookIsbn13").value("978-3-16-148410-0"));

        verify(bookAuthorService, times(1)).findBooksByAuthorId(authorId);
    }




    @Test
    public void testSearchBooksByKeyword() throws Exception {
        String keyword = "book";
        Pageable pageable = PageRequest.of(0, 3);
        BookInfoResponseDto bookInfoResponseDto = new BookInfoResponseDto(1L, "Book Title", "Publisher", "Category", "Author", new BigDecimal(10000), new BigDecimal(12000), "url", 101L);
        Page<BookInfoResponseDto> page = new PageImpl<>(Collections.singletonList(bookInfoResponseDto), pageable, 1);
        when(bookSearchService.searchBooksByKeyword2(keyword, pageable)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                        .param("searchKeyword", keyword)
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookTitle").value("Book Title"));
    }



}
