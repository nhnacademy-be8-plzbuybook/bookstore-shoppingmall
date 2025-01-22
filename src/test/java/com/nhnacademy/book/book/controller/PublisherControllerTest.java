package com.nhnacademy.book.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.dto.request.PublisherRegisterDto;
import com.nhnacademy.book.book.dto.response.PublisherResponseDto;
import com.nhnacademy.book.book.service.Impl.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublisherController.class)
class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @Autowired
    private ObjectMapper objectMapper;

    private PublisherRegisterDto publisherRegisterDto;
    private PublisherResponseDto publisherResponseDto;

    @BeforeEach
    void setUp() {
        publisherRegisterDto = new PublisherRegisterDto();
        publisherRegisterDto.setPublisherName("Test Publisher");
        publisherResponseDto = new PublisherResponseDto();
        publisherResponseDto.setPublisherName("Test Publisher");
    }

    @Test
    void createPublisherTest() throws Exception {

        Mockito.doAnswer(invocation -> null).when(publisherService).createPublisher(any(PublisherRegisterDto.class));

        mockMvc.perform(post("/api/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisherRegisterDto)))
                .andDo(print()) // 로그 출력
                .andExpect(status().isCreated());

        Mockito.verify(publisherService, Mockito.times(1)).createPublisher(any(PublisherRegisterDto.class));

    }

    @Test
    void deletePublisherTest() throws Exception {

        Mockito.doNothing().when(publisherService).deletePublisher(anyLong());

        mockMvc.perform(delete("/api/publishers/{publisherId}",1L))
                .andDo(print()) // 로그 출력
                .andExpect(status().isNoContent()); // HTTP 200 응답 확인


        Mockito.verify(publisherService, Mockito.times(1)).deletePublisher(1L);

    }

    @Test
    void getPublisherTest() throws Exception {
        Mockito.when(publisherService.findPublisherById(anyLong())).thenReturn(publisherResponseDto);

        mockMvc.perform(get("/api/publishers/{publisherId}" ,1L))
                .andDo(print()) // 로그 출력
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(publisherResponseDto)));

        Mockito.verify(publisherService, Mockito.times(1)).findPublisherById(anyLong());
    }

}
