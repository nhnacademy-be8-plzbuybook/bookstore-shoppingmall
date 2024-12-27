package com.nhnacademy.book.wrappingPaper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.handler.GlobalExceptionHandler;
import com.nhnacademy.book.member.domain.dto.ErrorResponseDto;
import com.nhnacademy.book.wrappingPaper.dto.*;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import com.nhnacademy.book.wrappingPaper.service.impl.WrappingPaperServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WrappingPaperController.class)
class WrappingPaperControllerTest {
    @MockBean
    private WrappingPaperServiceImpl wrappingPaperService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_URL = "/api/wrapping-papers";
    private WrappingPaper existingWrappingPaper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new WrappingPaperController(wrappingPaperService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        existingWrappingPaper = new WrappingPaper(1L, "test", new BigDecimal("1000"), 100L, LocalDateTime.now(), "/image/path");
    }

    @Test
    void getWrappingPaper() throws Exception {
        //given
        long id = existingWrappingPaper.getId();
        WrappingPaperDto wrappingPaperDto = new WrappingPaperDto(existingWrappingPaper);

        when(wrappingPaperService.getWrappingPaper(id)).thenReturn(wrappingPaperDto);

        //when
        mockMvc.perform(get(BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(wrappingPaperDto)));
    }

    @Test
    void getWrappingPaper_not_found() throws Exception {
        //given
        long id = existingWrappingPaper.getId();

        when(wrappingPaperService.getWrappingPaper(id)).thenThrow(new NotFoundException(id + "wrapping paper not found!"));

        //when
        MvcResult result = mockMvc.perform(get(BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        String response = result.getResponse().getContentAsString();
        ErrorResponseDto errorResponseDto = objectMapper.readValue(response, ErrorResponseDto.class);
        assertEquals(404, errorResponseDto.getStatus());
        assertEquals(id + "wrapping paper not found!", errorResponseDto.getMessage());
    }

    @Test
    void getWrappingPapers() throws Exception {
        //given
        List<WrappingPaperDto> wrappingPaperDtos = List.of(new WrappingPaperDto(existingWrappingPaper));
        when(wrappingPaperService.getWrappingPapers()).thenReturn(wrappingPaperDtos);

        //when
        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(wrappingPaperDtos)));
    }

    @Test
    void getWrappingPapers_return_null() throws Exception {
        //given
        List<WrappingPaperDto> wrappingPaperDtos = null;
        when(wrappingPaperService.getWrappingPapers()).thenReturn(wrappingPaperDtos);

        //when
        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void createWrappingPaper() throws Exception {
        //given
        WrappingPaperSaveRequestDto saveRequest = new WrappingPaperSaveRequestDto("test", new BigDecimal("1000"), 100L);
        WrappingPaperSaveResponseDto saveResponse = new WrappingPaperSaveResponseDto(1L);

        when(wrappingPaperService.createWrappingPaper(any(WrappingPaperSaveRequestDto.class), any(MultipartFile.class))).thenReturn(saveResponse);
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test-image.png",
                "image/png",
                "mock image content".getBytes()
        );
        // Mock @RequestPart WrappingPaperSaveRequestDto as JSON
        MockMultipartFile saveRequestPart = new MockMultipartFile(
                "saveRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(saveRequest)
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
                        .file(saveRequestPart)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(saveResponse)));
        verify(wrappingPaperService).createWrappingPaper(any(WrappingPaperSaveRequestDto.class), any(MultipartFile.class));
    }

    @Test
    void createWrappingPaper_invalid_argument() throws Exception {
        //given
        WrappingPaperSaveRequestDto saveRequest = new WrappingPaperSaveRequestDto(" ", new BigDecimal("1000"), 100L);
        WrappingPaperSaveResponseDto saveResponse = new WrappingPaperSaveResponseDto(1L);

        when(wrappingPaperService.createWrappingPaper(any(WrappingPaperSaveRequestDto.class), any(MultipartFile.class))).thenReturn(saveResponse);
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test-image.png",
                "image/png",
                "mock image content".getBytes()
        );
        MockMultipartFile saveRequestPart = new MockMultipartFile(
                "saveRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(saveRequest)
        );

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
                        .file(saveRequestPart)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

//        String response = mvcResult.getResponse().getContentAsString();
//        ErrorResponseDto errorResponseDto = objectMapper.readValue(response, ErrorResponseDto.class);
//        assertEquals(400, errorResponseDto.getStatus());
//        assertTrue(errorResponseDto.getMessage().contains("name"));
        verify(wrappingPaperService, never()).createWrappingPaper(any(WrappingPaperSaveRequestDto.class), any(MultipartFile.class));
    }

    @Test
    void createWrappingPaper_without_imageFile() throws Exception {
        //given
        WrappingPaperSaveRequestDto saveRequest = new WrappingPaperSaveRequestDto("test", new BigDecimal("1000"), 100L);
        WrappingPaperSaveResponseDto saveResponse = new WrappingPaperSaveResponseDto(1L);

        when(wrappingPaperService.createWrappingPaper(any(WrappingPaperSaveRequestDto.class), any(MultipartFile.class))).thenReturn(saveResponse);
        MockMultipartFile saveRequestPart = new MockMultipartFile(
                "saveRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(saveRequest)
        );

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
                        .file(saveRequestPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ErrorResponseDto errorResponseDto = objectMapper.readValue(response, ErrorResponseDto.class);
        assertEquals(400, errorResponseDto.getStatus());
        assertTrue(errorResponseDto.getMessage().contains("imageFile"));
        verify(wrappingPaperService, never()).createWrappingPaper(any(WrappingPaperSaveRequestDto.class), any(MultipartFile.class));
    }

    @Test
    void modifyWrappingPaper() throws Exception {
        //given
        long id = 1L;
        WrappingPaperUpdateRequestDto updateRequest = new WrappingPaperUpdateRequestDto("test", new BigDecimal("1000"), 100L, "/default/image");
        WrappingPaperUpdateResponseDto updateResponse = new WrappingPaperUpdateResponseDto(1L);

        when(wrappingPaperService.modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class), any(MultipartFile.class))).thenReturn(updateResponse);
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test-image.png",
                "image/png",
                "mock image content".getBytes()
        );
        MockMultipartFile updateRequestPart = new MockMultipartFile(
                "updateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(updateRequest)
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/" + id)
                        .file(updateRequestPart)
                        .file(imageFile)
                        .with(request -> {
                            request.setMethod("PUT"); // HTTP 메서드를 PUT으로 변경
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updateResponse)));
        verify(wrappingPaperService).modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class), any(MultipartFile.class));
    }

//    @Test
//    void modifyWrappingPaper_without_imageFile() throws Exception {
//        //given
//        long id = 1L;
//        WrappingPaperUpdateRequestDto updateRequest = new WrappingPaperUpdateRequestDto("test", new BigDecimal("1000"), 100L, "/default/image");
//        WrappingPaperUpdateResponseDto updateResponse = new WrappingPaperUpdateResponseDto(1L);
//
//        when(wrappingPaperService.modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class), any(MultipartFile.class))).thenReturn(updateResponse);
//        MockMultipartFile updateRequestPart = new MockMultipartFile(
//                "updateRequest",
//                "",
//                "application/json",
//                objectMapper.writeValueAsBytes(updateRequest)
//        );
//
//        //when
//        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/" + id)
//                        .file(updateRequestPart)
//                        .with(request -> {
//                            request.setMethod("PUT"); // HTTP 메서드를 PUT으로 변경
//                            return request;
//                        })
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(updateResponse)));
//        verify(wrappingPaperService).modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class), any(MultipartFile.class));
//    }

    @Test
    void modifyWrappingPaper_invalid_argument() throws Exception {
        //given
        long id = 1L;
        WrappingPaperUpdateRequestDto updateRequest = new WrappingPaperUpdateRequestDto(" ", new BigDecimal("1000"), 100L, "/default/image");
        WrappingPaperUpdateResponseDto updateResponse = new WrappingPaperUpdateResponseDto(1L);

        when(wrappingPaperService.modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class), any(MultipartFile.class))).thenReturn(updateResponse);
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test-image.png",
                "image/png",
                "mock image content".getBytes()
        );
        MockMultipartFile updateRequestPart = new MockMultipartFile(
                "updateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(updateRequest)
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/" + id)
                        .file(updateRequestPart)
                        .file(imageFile)
                        .with(request -> {
                            request.setMethod("PUT"); // HTTP 메서드를 PUT으로 변경
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(wrappingPaperService, never()).modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class), any(MultipartFile.class));
    }

    @Test
    void removeWrappingPaper() throws Exception {
        //given
        long id = existingWrappingPaper.getId();

        doNothing().when(wrappingPaperService).removeWrappingPaper(id);

        //when
        mockMvc.perform(delete(BASE_URL + "/" + id))
                .andExpect(status().isNoContent());
        verify(wrappingPaperService).removeWrappingPaper(id);
    }

    @Test
    void removeWrappingPaper_not_found() throws Exception {
        //given
        long id = existingWrappingPaper.getId();

        doThrow(new NotFoundException(id + "wrapping paper not found!")).when(wrappingPaperService).removeWrappingPaper(id);

        //when
        MvcResult result = mockMvc.perform(delete(BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ErrorResponseDto errorResponseDto = objectMapper.readValue(response, ErrorResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponseDto.getStatus());
        assertEquals(id + "wrapping paper not found!", errorResponseDto.getMessage());
        verify(wrappingPaperService).removeWrappingPaper(id);
    }
}