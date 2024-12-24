package com.nhnacademy.book.booktest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.controller.BookController;
import com.nhnacademy.book.book.dto.request.AuthorRequestDto;
import com.nhnacademy.book.book.dto.request.BookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.service.Impl.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    List<BookDetailResponseDto> bookDetailResponseDtoList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        BookRegisterDto bookRegisterDto = new BookRegisterDto();
        bookRegisterDto.setBookTitle("test title");
        bookRegisterDto.setBookIsbn13("test isbn");
        bookRegisterDto.setBookIndex("test index");
        bookRegisterDto.setBookDescription("test description");
        bookRegisterDto.setBookPriceStandard(new BigDecimal(1000L));
        bookRegisterDto.setBookPubDate(LocalDate.of(2000,1,1));
        bookRegisterDto.setPublisherId(1L);


        BookDetailResponseDto bookDetailResponseDto = new BookDetailResponseDto();
        bookDetailResponseDto.setBookId(4L);
        bookDetailResponseDto.setBookTitle("test title");
        bookDetailResponseDto.setBookIsbn13("test isbn");
        bookDetailResponseDto.setBookIndex("test index");
        bookDetailResponseDto.setBookDescription("test description");
        bookDetailResponseDto.setBookPriceStandard(new BigDecimal(1000L));
        bookDetailResponseDto.setPublisherName("test");
        bookDetailResponseDto.setBookPubDate(LocalDate.of(2000,1,1));

        BookDetailResponseDto bookDetailResponseDto2 = new BookDetailResponseDto();
        bookDetailResponseDto2.setBookId(5L);
        bookDetailResponseDto2.setBookTitle("test title5");
        bookDetailResponseDto2.setBookIsbn13("test isbn5");
        bookDetailResponseDto2.setBookIndex("test index5");
        bookDetailResponseDto2.setBookDescription("test description5");
        bookDetailResponseDto2.setBookPriceStandard(new BigDecimal(1000L));
        bookDetailResponseDto2.setPublisherName("test5");
        bookDetailResponseDto2.setBookPubDate(LocalDate.of(2000,1,1));

        BookDetailResponseDto bookDetailResponseDto3 = new BookDetailResponseDto();
        bookDetailResponseDto3.setBookId(6L);
        bookDetailResponseDto3.setBookTitle("test title6");
        bookDetailResponseDto3.setBookIsbn13("test isbn6");
        bookDetailResponseDto3.setBookIndex("test index6");
        bookDetailResponseDto3.setBookDescription("test description6");
        bookDetailResponseDto3.setBookPriceStandard(new BigDecimal(1000L));
        bookDetailResponseDto3.setPublisherName("test6");
        bookDetailResponseDto3.setBookPubDate(LocalDate.of(2000,1,1));

        bookDetailResponseDtoList.add(bookDetailResponseDto);
        bookDetailResponseDtoList.add(bookDetailResponseDto2);
        bookDetailResponseDtoList.add(bookDetailResponseDto3);



    }

    @Test
    void createBook() throws Exception {
        BookRegisterDto bookRegisterDto = new BookRegisterDto();
        bookRegisterDto.setBookTitle("test title2");
        bookRegisterDto.setBookIsbn13("test isbn2");
        bookRegisterDto.setBookIndex("test index2");
        bookRegisterDto.setBookDescription("test description2");
        bookRegisterDto.setBookPriceStandard(new BigDecimal(1000L));
        bookRegisterDto.setBookPubDate(LocalDate.of(2000,1,1));
        bookRegisterDto.setPublisherId(2L);

        Mockito.doNothing().when(bookService).registerBook(any(BookRegisterDto.class));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRegisterDto)))
                .andDo(print()) // 로그 출력
                .andExpect(status().isOk());

        Mockito.verify(bookService, Mockito.times(1)).registerBook(any(BookRegisterDto.class));

    }

    @Test
    void deleteBook() throws Exception {
        Mockito.doNothing().when(bookService).deleteBook(any(Long.class));

        mockMvc.perform(delete("/api/books/{bookId}", 1L))
                .andDo(print()) // 로그 출력
                .andExpect(status().isOk());

        Mockito.verify(bookService, Mockito.times(1)).deleteBook(any(Long.class));
    }

    @Test
    void getBookDetail() throws Exception {
        BookDetailResponseDto bookDetailResponseDto = new BookDetailResponseDto();
        bookDetailResponseDto.setBookId(3L);
        bookDetailResponseDto.setBookTitle("test title");
        bookDetailResponseDto.setBookIsbn13("test isbn");
        bookDetailResponseDto.setBookIndex("test index");
        bookDetailResponseDto.setBookDescription("test description");
        bookDetailResponseDto.setBookPriceStandard(new BigDecimal(1000L));
        bookDetailResponseDto.setPublisherName("test");
        bookDetailResponseDto.setBookPubDate(LocalDate.of(2000,1,1));


        when(bookService.getBookDetail(anyLong())).thenReturn(bookDetailResponseDto);


        mockMvc.perform(get("/api/books/{bookId}",3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // 로그 출력
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookDetailResponseDto)));

    }

    @Test
    void getAllBooks() throws Exception {

        when(bookService.getAllBooks()).thenReturn(bookDetailResponseDtoList);

        mockMvc.perform(get("/api/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookDetailResponseDtoList)));

        Mockito.verify(bookService, Mockito.times(1)).getAllBooks();


    }

    @Test
    void updateBook() throws Exception {

        Mockito.doNothing().when(bookService).updateBook(anyLong(), any(BookRegisterDto.class));

        BookRegisterDto bookUpdateRequest = new BookRegisterDto();
        bookUpdateRequest.setBookTitle("Updated Book Title");
        bookUpdateRequest.setBookIsbn13("Updated ISBN");
        bookUpdateRequest.setBookIndex("Updated Index");
        bookUpdateRequest.setBookDescription("Updated Description");
        bookUpdateRequest.setBookPriceStandard(new BigDecimal(1500));
        bookUpdateRequest.setBookPubDate(LocalDate.of(2020, 5, 20));
        bookUpdateRequest.setPublisherId(2L);


        mockMvc.perform(put("/api/books/{bookId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookUpdateRequest)))
                .andDo(print()) // 로그 출력
                .andExpect(status().isOk());

        Mockito.verify(bookService).updateBook(anyLong(), any(BookRegisterDto.class));

    }

}
