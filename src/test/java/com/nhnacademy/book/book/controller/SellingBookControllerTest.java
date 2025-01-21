package com.nhnacademy.book.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellingBookController.class)
class SellingBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellingBookService sellingBookService;

    @Autowired
    private ObjectMapper objectMapper;

    private SellingBookAndBookResponseDto sellingBookAndBookResponseDto;

    @BeforeEach
    void setUp() {
        // DTO 수동 초기화
        sellingBookAndBookResponseDto = SellingBookAndBookResponseDto.builder()
                .sellingBookId(1L)
                .bookId(1L)
                .bookTitle("Test Book")
                .sellingBookPrice(BigDecimal.valueOf(15000))
                .sellingBookPackageable(true)
                .sellingBookStock(10)
                .sellingBookStatus(SellingBook.SellingBookStatus.SELLING)
                .used(false)
                .sellingBookViewCount(100L)
                .publisher("Test Publisher")
                .categories(List.of("Category1", "Category2"))
                .authors(List.of("Author1", "Author2"))
                .build();

        // Mock 설정
        Mockito.when(sellingBookService.getBooks(any(), any()))
                .thenReturn(new PageImpl<>(List.of(sellingBookAndBookResponseDto)));

        Mockito.doNothing().when(sellingBookService).registerSellingBooks(any(SellingBookRegisterDto.class));
        Mockito.doNothing().when(sellingBookService).deleteSellingBook(anyLong());
    }

    @Test
    void testGetBooks() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/selling-books")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "sellingBookId")
                .param("sortDir", "desc")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookTitle").value("Test Book"))
                .andExpect(jsonPath("$.content[0].publisher").value("Test Publisher"));
    }

    @Test
    void testRegisterSellingBooks() throws Exception {
        SellingBookRegisterDto registerDto = new SellingBookRegisterDto();
        registerDto.setBookId(1L);
        registerDto.setSellingBookPrice(BigDecimal.valueOf(15000));
        registerDto.setSellingBookPackageable(true);
        registerDto.setSellingBookStock(10);
        registerDto.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING);
        registerDto.setUsed(false);
        registerDto.setSellingBookViewCount(100L);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/selling-books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.sellingBookPrice").value(15000))
                .andExpect(jsonPath("$.sellingBookStock").value(10));
    }

    @Test
    void testDeleteSellingBook() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/selling-books/1"));

        result.andExpect(status().isNoContent());
    }

    @Test
    void testGetSellingBook() throws Exception {
        BookDetailResponseDto responseDto = new BookDetailResponseDto();
        responseDto.setSellingBookId(1L);
        responseDto.setBookTitle("Detail Book");

        Mockito.when(sellingBookService.getSellingBook(anyLong())).thenReturn(responseDto);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/selling-books/1"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.bookTitle").value("Detail Book"));
    }

    @Test
    void testGetSellingBooksByViewCount() throws Exception {
        Mockito.when(sellingBookService.getSellingBooksByViewCount(any()))
                .thenReturn(List.of(sellingBookAndBookResponseDto));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/selling-books/view-count")
                .param("sortDirection", "desc"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookTitle").value("Test Book"));
    }
}