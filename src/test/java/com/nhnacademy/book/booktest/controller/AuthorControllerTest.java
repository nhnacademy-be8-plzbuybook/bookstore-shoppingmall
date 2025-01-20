//package com.nhnacademy.book.booktest.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.netflix.discovery.converters.Auto;
//import com.nhnacademy.book.book.controller.AuthorController;
//import com.nhnacademy.book.book.dto.request.AuthorRequestDto;
//import com.nhnacademy.book.book.dto.response.AuthorResponseDto;
//import com.nhnacademy.book.book.elastic.repository.AuthorSearchRepository;
//import com.nhnacademy.book.book.service.Impl.AuthorService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(AuthorController.class)
//public class AuthorControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthorService authorService;
//
//    @MockBean
//    private AuthorSearchRepository authorSearchRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private AuthorRequestDto authorRequestDto;
//    private AuthorResponseDto authorResponseDto;
//
//
//    @BeforeEach
//    void setUp() {
//        authorRequestDto = new AuthorRequestDto("Test Author");
//        authorResponseDto = new AuthorResponseDto(1L, "Test Author");
//    }
//
//    @Test
//    void createAuthor() throws Exception {
//        Mockito.doAnswer(invocation -> null).when(authorService).createAuthor(any(AuthorRequestDto.class));
//
//        mockMvc.perform(post("/api/authors")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authorRequestDto)))
//                .andDo(print()) // 로그 출력
//                .andExpect(status().isOk());
//
//        Mockito.verify(authorService, Mockito.times(1)).createAuthor(any(AuthorRequestDto.class));
//    }
//
//    @Test
//    void getAuthor() throws Exception {
//        Mockito.when(authorService.getAuthorById(anyLong())).thenReturn(authorResponseDto);
//
//        mockMvc.perform(get("/api/authors/" + authorResponseDto.getAuthorId()))
//                .andDo(print()) // 로그 출력
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(authorResponseDto)));
//
//        Mockito.verify(authorService, Mockito.times(1)).getAuthorById(anyLong());
//    }
//
////    @Test
////    void getAuthors() throws Exception {
////        List<AuthorResponseDto> authors = Arrays.asList(
////                new AuthorResponseDto(1L, "Author1"),
////                new AuthorResponseDto(2L, "Author2")
////        );
////
////        Mockito.when(authorService.getAllAuthors()).thenReturn(authors);
////
////        mockMvc.perform(get("/api/authors"))
////                .andDo(print()) // 로그 출력
////                .andExpect(status().isOk())
////                .andExpect(content().json(objectMapper.writeValueAsString(authors)));
////
////        Mockito.verify(authorService, Mockito.times(1)).getAllAuthors();
////    }
//
//    @Test
//    void deleteAuthor() throws Exception {
//        Mockito.doNothing().when(authorService).deleteAuthorById(anyLong());
//
//        mockMvc.perform(delete("/api/authors/{authorId}", 1L))
//                .andDo(print()) // 로그 출력
//                .andExpect(status().isOk()); // HTTP 200 응답 확인
//
//        Mockito.verify(authorService, Mockito.times(1)).deleteAuthorById(1L);
//    }
//
//
//}
