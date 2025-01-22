package com.nhnacademy.book.wrappingPaper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.handler.GlobalExceptionHandler;
import com.nhnacademy.book.member.domain.dto.ErrorResponseDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingCreateSaveRequestDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperUpdateRequestDto;
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

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_URL = "/api/wrapping-papers";
    private WrappingPaper existingWrappingPaper;

    @BeforeEach
    void setup() {
        // LocalDateTime 지원을 위한 모듈 추가
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(new WrappingPaperController(wrappingPaperService))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8) // 응답 utf-8로 설정
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

        when(wrappingPaperService.getWrappingPaper(id)).thenThrow(new NotFoundException("포장지를 찾을 수 없습니다. 포장지 아이디: " + id));

        //when
        MvcResult result = mockMvc.perform(get(BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        String response = result.getResponse().getContentAsString();
        ErrorResponseDto errorResponseDto = objectMapper.readValue(response, ErrorResponseDto.class);
        assertEquals(404, errorResponseDto.getStatus());
        assertEquals("포장지를 찾을 수 없습니다. 포장지 아이디: " + id, errorResponseDto.getMessage());
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
        when(wrappingPaperService.getWrappingPapers()).thenReturn(null);

        //when
        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void createWrappingPaper() throws Exception {
        //given
        MockMultipartFile mockImageFile = new MockMultipartFile(
                "imageFile",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "dummy image content".getBytes()
        );
        Long savedId = 1L;

        when(wrappingPaperService.createWrappingPaper(any(WrappingCreateSaveRequestDto.class))).thenReturn(savedId);

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
                        .file(mockImageFile)
                        .param("name", "name")
                        .param("price", "1000")
                        .param("stock", "100")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(savedId.toString()))
                .andReturn();
        assertEquals(savedId.toString(), result.getResponse().getContentAsString());
    }


    @Test
    void createWrappingPaper_invalid_argument() throws Exception {
        MockMultipartFile mockImageFile = new MockMultipartFile(
                "imageFile",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "dummy image content".getBytes()
        );

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
                        .file(mockImageFile)
                        .param("name", " ")
                        .param("price", "1000")
                        .param("stock", "100")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void createWrappingPaper_without_imageFile() throws Exception {
        //given
        Long savedId = 1L;
        when(wrappingPaperService.createWrappingPaper(any(WrappingCreateSaveRequestDto.class))).thenReturn(savedId);

        //when
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL)
                        .param("name", "name")
                        .param("price", "1000")
                        .param("stock", "100")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ErrorResponseDto errorResponseDto = objectMapper.readValue(response, ErrorResponseDto.class);
        assertEquals(400, errorResponseDto.getStatus());
        verify(wrappingPaperService, never()).createWrappingPaper(any(WrappingCreateSaveRequestDto.class));
    }

    @Test
    void modifyWrappingPaper() throws Exception {
        //given
        Long id = 1L;

        when(wrappingPaperService.modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class))).thenReturn(id);
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test-image.png",
                "image/png",
                "mock image content".getBytes()
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/" + id)
                        .file(imageFile)
                        .param("name", "updatedName")
                        .param("price", "1131")
                        .param("stock", "110")
                        .with(request -> {
                            request.setMethod("PUT"); // HTTP 메서드를 PUT으로 변경
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(id.toString()));
        verify(wrappingPaperService).modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class));
    }

    @Test
    void modifyWrappingPaper_without_imageFile() throws Exception {
        //given
        Long id = 1L;

        when(wrappingPaperService.modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class))).thenReturn(id);

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/" + id)
                        .param("name", "updatedName")
                        .param("price", "1131")
                        .param("stock", "110")
                        .with(request -> {
                            request.setMethod("PUT"); // HTTP 메서드를 PUT으로 변경
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(id.toString()));
        verify(wrappingPaperService).modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class));
    }

    @Test
    void modifyWrappingPaper_invalid_argument() throws Exception {
        //given
        Long id = 1L;

        when(wrappingPaperService.modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class))).thenReturn(id);

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/" + id)
                        .param("name", " ")
                        .param("price", "1131")
                        .param("stock", "110")
                        .with(request -> {
                            request.setMethod("PUT"); // HTTP 메서드를 PUT으로 변경
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(wrappingPaperService, never()).modifyWrappingPaper(anyLong(), any(WrappingPaperUpdateRequestDto.class));
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

        doThrow(new NotFoundException("포장지를 찾을 수 없습니다. 포장지 아이디: " + id)).when(wrappingPaperService).removeWrappingPaper(id);

        //when
        MvcResult result = mockMvc.perform(delete(BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ErrorResponseDto errorResponseDto = objectMapper.readValue(response, ErrorResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponseDto.getStatus());
        assertEquals("포장지를 찾을 수 없습니다. 포장지 아이디: " + id, errorResponseDto.getMessage());
        verify(wrappingPaperService).removeWrappingPaper(id);
    }
}