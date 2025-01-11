//package com.nhnacademy.book.wrappingPaper.service.impl;
//
//import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
//import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
//import com.nhnacademy.book.wrappingPaper.dto.*;
//import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
//import com.nhnacademy.book.wrappingPaper.repository.WrappingPaperRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class WrappingPaperServiceImplTest {
//    @Mock
//    private WrappingPaperRepository repository;
//    @InjectMocks
//    private WrappingPaperServiceImpl service;
//    private final WrappingPaper existingWrappingPaper;
//
//    public WrappingPaperServiceImplTest() {
//        existingWrappingPaper = new WrappingPaper(1L, "name", new BigDecimal("3000"), 100L, LocalDateTime.now(), "/image/path");
//    }
//
//    @Test
//    void getWrappingPaper() {
//        //given
//        long id = existingWrappingPaper.getId();
//
//        when(repository.findById(id)).thenReturn(Optional.of(existingWrappingPaper));
//
//        //when
//        WrappingPaperDto wrappingPaperDto = service.getWrappingPaper(id);
//
//        //then
//        verify(repository).findById(id);
//        assertNotNull(wrappingPaperDto);
//        assertEquals("name", wrappingPaperDto.getName());
//        assertEquals(0, wrappingPaperDto.getPrice().compareTo(new BigDecimal("3000")));
//        assertEquals(100L, wrappingPaperDto.getStock());
//        assertEquals("/image/path", wrappingPaperDto.getImagePath());
//    }
//
//    @Test
//    void getWrappingPaper_not_found() {
//        //given
//        long notExistsId = 123L;
//
//        when(repository.findById(notExistsId)).thenReturn(Optional.empty());
//
//        //when
//        NotFoundException e = assertThrows(NotFoundException.class, () -> service.getWrappingPaper(notExistsId));
//
//        //then
//        verify(repository).findById(notExistsId);
//        assertEquals(notExistsId + "wrapping paper not found!", e.getMessage());
//    }
//
//    @Test
//    void getWrappingPapers() {
//        //given
//        when(repository.findAll()).thenReturn(List.of(existingWrappingPaper));
//
//        //when
//        List<?> result = service.getWrappingPapers();
//
//        //then
//        assertFalse(result.isEmpty());
//        assertInstanceOf(WrappingPaperDto.class, result.getFirst());
//    }
//
//    @Test
//    void getWrappingPapers_return_null() {
//        //given
//        when(repository.findAll()).thenReturn(Collections.emptyList());
//
//        //when
//        List<?> result = service.getWrappingPapers();
//
//        //then
//        assertNull(result);
//    }
//
//    @Test
//    void createWrappingPaper() {
//        //given
//        WrappingCreateSaveRequestDto saveRequest = new WrappingCreateSaveRequestDto("name", new BigDecimal("3000"), 100L);
//        MultipartFile mockMultipartFile = mock(MultipartFile.class);
//        String imagePath = "/image/path";
//        WrappingPaper savedWrappingPaper = new WrappingPaper(2L, saveRequest.getName(), saveRequest.getPrice(), saveRequest.getStock(), LocalDateTime.now(), imagePath);
//
//        when(repository.existsByName(saveRequest.getName())).thenReturn(false);
//        when(repository.save(any(WrappingPaper.class))).thenReturn(savedWrappingPaper);
//
//        //when
//        WrappingPaperCreateResponseDto saveResponse = service.createWrappingPaper(saveRequest, mockMultipartFile);
//
//        //then
//        assertNotNull(saveResponse);
//        assertEquals(savedWrappingPaper.getId(), saveResponse.id());
//        verify(repository).existsByName(saveRequest.getName());
//        verify(repository).save(any(WrappingPaper.class));
//    }
//
//    @Test
//    void createWrappingPaper_conflict() {
//        //given
//        WrappingCreateSaveRequestDto saveRequest = new WrappingCreateSaveRequestDto("name", new BigDecimal("3000"), 100L);
//        MultipartFile mockMultipartFile = mock(MultipartFile.class);
//
//        when(repository.existsByName(saveRequest.getName())).thenReturn(true);
//
//        //when
//        ConflictException e = assertThrows(ConflictException.class, () -> service.createWrappingPaper(saveRequest, mockMultipartFile));
//
//        //then
//        assertEquals("wrapping paper: [" + saveRequest.getName() + "] is already exists!", e.getMessage());
//        verify(repository).existsByName(saveRequest.getName());
//    }
//
//    @Test
//    void modifyWrappingPaper_with_image() {
//        //given
//        long existingId = existingWrappingPaper.getId();
//        MultipartFile mockMultipartFile = mock(MultipartFile.class);
//        WrappingPaperUpdateRequestDto updateRequest = new WrappingPaperUpdateRequestDto("update", new BigDecimal("2000"), 20L, "/default/path");
//        WrappingPaper spyWrappingPaper = spy(existingWrappingPaper);
//
//        when(repository.findById(existingId)).thenReturn(Optional.of(spyWrappingPaper));
//
//        //when
//        WrappingPaperUpdateResponseDto updateResponse = service.modifyWrappingPaper(existingId, updateRequest, mockMultipartFile);
//
//        //then
//        verify(repository).findById(existingId);
//        assertEquals(updateResponse.id(), existingId);
//        assertEquals(updateRequest.name(), spyWrappingPaper.getName());
//        assertEquals(0, updateRequest.price().compareTo(spyWrappingPaper.getPrice()));
//        assertEquals(updateRequest.stock(), spyWrappingPaper.getStock());
//        assertEquals("/update/path", spyWrappingPaper.getImagePath());
//    }
//
//    @Test
//    void modifyWrappingPaper_without_image() {
//        //given
//        long existingId = existingWrappingPaper.getId();
//        WrappingPaperUpdateRequestDto updateRequest = new WrappingPaperUpdateRequestDto("update", new BigDecimal("2000"), 20L, "/default/path");
//        WrappingPaper spyWrappingPaper = spy(existingWrappingPaper);
//
//        when(repository.findById(existingId)).thenReturn(Optional.of(spyWrappingPaper));
//
//        //when
//        WrappingPaperUpdateResponseDto updateResponse = service.modifyWrappingPaper(existingId, updateRequest, null);
//
//        //then
//        verify(repository).findById(existingId);
//        assertEquals(updateResponse.id(), existingId);
//        assertEquals(updateRequest.name(), spyWrappingPaper.getName());
//        assertEquals(0, updateRequest.price().compareTo(spyWrappingPaper.getPrice()));
//        assertEquals(updateRequest.stock(), spyWrappingPaper.getStock());
//        assertEquals("/default/path", spyWrappingPaper.getImagePath());
//    }
//
//    @Test
//    void modifyWrappingPaper_not_found() {
//        //given
//        long notExistsId = 123L;
//        WrappingPaperUpdateRequestDto updateRequest = new WrappingPaperUpdateRequestDto("update", new BigDecimal("2000"), 20L, "/default/path");
//        when(repository.findById(notExistsId)).thenReturn(Optional.empty());
//
//        //when
//        NotFoundException e = assertThrows(NotFoundException.class, () -> service.modifyWrappingPaper(notExistsId, updateRequest, null));
//
//        //then
//        verify(repository).findById(notExistsId);
//        assertEquals(notExistsId + "wrapping paper not found!", e.getMessage());
//    }
//
//    @Test
//    void removeWrappingPaper() {
//        //given
//        long existingId = existingWrappingPaper.getId();
//
//        when(repository.existsById(existingId)).thenReturn(false);
//
//        //when
//        service.removeWrappingPaper(existingId);
//
//        //then
//        verify(repository).existsById(existingId);
//        verify(repository).deleteById(existingId);
//    }
//
//    @Test
//    void removeWrappingPaper_not_found() {
//        //given
//        long notExistsId = existingWrappingPaper.getId();
//
//        when(repository.existsById(notExistsId)).thenReturn(true);
//
//        //when
//        NotFoundException e = assertThrows(NotFoundException.class, () -> service.removeWrappingPaper(notExistsId));
//
//        //then
//        verify(repository).existsById(notExistsId);
//        assertEquals(notExistsId + "wrapping paper not found!", e.getMessage());
//    }
//}