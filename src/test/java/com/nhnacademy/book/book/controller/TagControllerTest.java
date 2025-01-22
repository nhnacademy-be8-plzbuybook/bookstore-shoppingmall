package com.nhnacademy.book.book.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.dto.request.TagRegisterDto;
import com.nhnacademy.book.book.dto.response.BookTagResponseDto;
import com.nhnacademy.book.book.dto.response.TagResponseDto;
import com.nhnacademy.book.book.service.Impl.BookTagService;
import com.nhnacademy.book.book.service.Impl.TagService;
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

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @MockBean
    private BookTagService bookTagService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSaveTag() throws Exception {
        TagRegisterDto requestDto = new TagRegisterDto();
        requestDto.setTagName("Test Tag");

        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        Mockito.verify(tagService).save(any(TagRegisterDto.class));
    }

    @Test
    void testGetAllTags() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        TagResponseDto responseDto = new TagResponseDto();
        responseDto.setTagName("Test Tag");
        Page<TagResponseDto> tags = new PageImpl<>(List.of(responseDto), pageable, 1);

        Mockito.when(tagService.findAll(pageable)).thenReturn(tags);

        mockMvc.perform(get("/api/tags")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tagName").value("Test Tag"));
    }

    @Test
    void testGetAllTagsWithKeyword() throws Exception {
        String keyword = "test";
        Pageable pageable = PageRequest.of(0, 10);
        TagResponseDto tagResponseDto = new TagResponseDto();
        tagResponseDto.setTagName("Test Tag");
        Page<TagResponseDto> tags = new PageImpl<>(List.of(tagResponseDto), pageable, 1);

        Mockito.when(tagService.searchTagsByKeyword(keyword, pageable)).thenReturn(tags);

        mockMvc.perform(get("/api/tags")
                        .param("keyword", keyword)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tagName").value("Test Tag"));
    }

    @Test
    void testGetTagNameByTagId() throws Exception {
        Long tagId = 1L;
        String tagName = "Test Tag";

        Mockito.when(tagService.findTagNameByTagId(tagId)).thenReturn(tagName);

        mockMvc.perform(get("/api/tags/{tagId}", tagId))
                .andExpect(status().isOk())
                .andExpect(content().string(tagName));
    }

    @Test
    void testDeleteTag() throws Exception {
        Long tagId = 1L;

        mockMvc.perform(delete("/api/tags/{tagId}", tagId))
                .andExpect(status().isNoContent());

        Mockito.verify(tagService).deleteTagById(tagId);
    }

    @Test
    void testGetAllBookTags() throws Exception {
        Long tagId = 1L;
        BookTagResponseDto responseDto = new BookTagResponseDto();
        responseDto.setBookName("Test Book");
        List<BookTagResponseDto> bookTags = List.of(responseDto);

        Mockito.when(bookTagService.getBookTagList(tagId)).thenReturn(bookTags);

        mockMvc.perform(get("/api/tags/{tag-id}/books", tagId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookName").value("Test Book"));
    }

    @Test
    void testSaveBookTag() throws Exception {
        Long bookId = 1L;
        Long tagId = 2L;

        mockMvc.perform(post("/api/books/{book-id}/tags/{tag-id}", bookId, tagId))
                .andExpect(status().isCreated());

        Mockito.verify(bookTagService).save(bookId, tagId);
    }

    @Test
    void testDeleteBookTag() throws Exception {
        Long bookId = 1L;
        Long tagId = 2L;

        mockMvc.perform(delete("/api/books/{book-id}/tags/{tag-id}", bookId, tagId))
                .andExpect(status().isNoContent());

        Mockito.verify(bookTagService).deleteBookTagList(bookId, tagId);
    }

    @Test
    void testGetBookTagsByBookId() throws Exception {
        Long bookId = 1L;
        BookTagResponseDto responseDto = new BookTagResponseDto();
        responseDto.setTagName("Test Tag");
        List<BookTagResponseDto> bookTags = List.of(responseDto);

        Mockito.when(bookTagService.getBookTagListByBookId(bookId)).thenReturn(bookTags);

        mockMvc.perform(get("/api/books/{book-id}/tags", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tagName").value("Test Tag"));
    }
}
