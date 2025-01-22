package com.nhnacademy.book.wrappingPaper.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.objectstorage.service.ObjectStorageService;
import com.nhnacademy.book.wrappingPaper.dto.WrappingCreateSaveRequestDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperUpdateRequestDto;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import com.nhnacademy.book.wrappingPaper.repository.WrappingPaperRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.nhnacademy.book.wrappingPaper.service.impl.WrappingPaperServiceImpl.WRAPPING_PAPER_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WrappingPaperServiceImplTest {
    @Mock
    private WrappingPaperRepository wrappingPaperRepository;
    @Mock
    private ObjectStorageService objectStorageService;
    @InjectMocks
    private WrappingPaperServiceImpl wrappingPaperService;
    private final WrappingPaper existingWrappingPaper;

    public WrappingPaperServiceImplTest() {
        existingWrappingPaper = new WrappingPaper(1L, "name", new BigDecimal("3000"), 100L, LocalDateTime.now(), "/image/path");
    }

    @DisplayName("포장지 조회")
    @Test
    void getWrappingPaper() {
        //given
        long id = existingWrappingPaper.getId();

        when(wrappingPaperRepository.findById(id)).thenReturn(Optional.of(existingWrappingPaper));

        //when
        WrappingPaperDto wrappingPaperDto = wrappingPaperService.getWrappingPaper(id);

        //then
        verify(wrappingPaperRepository).findById(id);
        assertNotNull(wrappingPaperDto);
        assertEquals("name", wrappingPaperDto.getName());
        assertEquals(0, wrappingPaperDto.getPrice().compareTo(new BigDecimal("3000")));
        assertEquals(100L, wrappingPaperDto.getStock());
        assertEquals("/image/path", wrappingPaperDto.getImagePath());
    }

    @DisplayName("포장지 조회: 포장지 없음")
    @Test
    void getWrappingPaper_wrappingPaper_not_found() {
        //given
        long id = existingWrappingPaper.getId();

        when(wrappingPaperRepository.findById(id)).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> wrappingPaperService.getWrappingPaper(id));

        //then
        verify(wrappingPaperRepository).findById(id);
        assertEquals("포장지를 찾을 수 없습니다. 포장지 아이디: " + id, exception.getMessage());
    }

    @DisplayName("포장지 목록 조회")
    @Test
    void getWrappingPapers() {
        //given
        when(wrappingPaperRepository.findAll()).thenReturn(List.of(existingWrappingPaper));

        //when
        List<WrappingPaperDto> result = wrappingPaperService.getWrappingPapers();

        //then
        assertFalse(result.isEmpty());
        WrappingPaperDto wrappingPaperDto = result.getFirst();
        assertNotNull(wrappingPaperDto);
        assertEquals("name", wrappingPaperDto.getName());
        assertEquals(0, wrappingPaperDto.getPrice().compareTo(new BigDecimal("3000")));
        assertEquals(100L, wrappingPaperDto.getStock());
        assertEquals("/image/path", wrappingPaperDto.getImagePath());

    }

    @DisplayName("포장지 목록조회: empty list")
    @Test
    void getWrappingPapers_return_null() {
        //given
        when(wrappingPaperRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        List<WrappingPaperDto> result = wrappingPaperService.getWrappingPapers();

        //then
        assertEquals(Collections.emptyList(), result);
        verify(wrappingPaperRepository).findAll();
    }

    @DisplayName("포장지 생성")
    @Test
    void createWrappingPaper() {
        //given
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        WrappingCreateSaveRequestDto saveRequest = new WrappingCreateSaveRequestDto("name", new BigDecimal("3000"), 100L, mockMultipartFile);
        String imagePath = "/image/path";
        WrappingPaper savedWrappingPaper = new WrappingPaper(2L, saveRequest.getName(), saveRequest.getPrice(), saveRequest.getStock(), LocalDateTime.now(), imagePath);

        when(wrappingPaperRepository.existsByName(saveRequest.getName())).thenReturn(false);
        when(wrappingPaperRepository.save(any(WrappingPaper.class))).thenReturn(savedWrappingPaper);
        when(objectStorageService.uploadObjects(List.of(mockMultipartFile))).thenReturn(List.of("uploaded/file/name"));

        //when
        Long result = wrappingPaperService.createWrappingPaper(saveRequest);

        //then
        assertNotNull(result);
        assertEquals(savedWrappingPaper.getId(), result);
        verify(objectStorageService).uploadObjects(List.of(mockMultipartFile));
        verify(wrappingPaperRepository).save(any(WrappingPaper.class));
    }

    @DisplayName("포장지 생성: 이름 중복")
    @Test
    void createWrappingPaper_duplicated_name() {
        //given
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        WrappingCreateSaveRequestDto saveRequest = new WrappingCreateSaveRequestDto("name", new BigDecimal("3000"), 100L, mockMultipartFile);

        when(wrappingPaperRepository.existsByName(saveRequest.getName())).thenReturn(true);

        //when
        ConflictException e = assertThrows(ConflictException.class,
                () -> wrappingPaperService.createWrappingPaper(saveRequest));

        //then
        assertEquals(saveRequest.getName() + "는 이미 존재하는 포장지입니다.", e.getMessage());
    }


    @DisplayName("포장지 수정: 포장지 없음")
    @Test
    void modifyWrappingPaper_wrappingPaper_not_found() {
        //given
        long existingId = existingWrappingPaper.getId();
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        WrappingPaperUpdateRequestDto updateRequest = new WrappingPaperUpdateRequestDto("update", new BigDecimal("2000"), 20L, mockMultipartFile);

        when(wrappingPaperRepository.findById(existingId)).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> wrappingPaperService.modifyWrappingPaper(existingId, updateRequest));

        //then
        assertEquals(WRAPPING_PAPER_NOT_FOUND_MSG + existingId, exception.getMessage());
    }


    @DisplayName("포장지 수정: 이미지 포함")
    @Test
    void modifyWrappingPaper_with_image() {
        //given
        long existingId = existingWrappingPaper.getId();
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        String updatedName = "updatedName";
        BigDecimal updatedPrice = BigDecimal.valueOf(2_000);
        Long updatedStock = 29L;
        String updatedImagePath = "updated/image";
        WrappingPaperUpdateRequestDto updateRequest = new WrappingPaperUpdateRequestDto(updatedName, updatedPrice, updatedStock, mockMultipartFile);
        WrappingPaper spyWrappingPaper = spy(existingWrappingPaper);

        when(wrappingPaperRepository.findById(existingId)).thenReturn(Optional.of(spyWrappingPaper));
        when(objectStorageService.uploadObjects(List.of(mockMultipartFile))).thenReturn(List.of("uploaded/file/name"));
        when(objectStorageService.getUrl(anyString())).thenReturn(updatedImagePath);

        //when
        Long result = wrappingPaperService.modifyWrappingPaper(existingId, updateRequest);

        //then
        assertNotNull(result);
        assertEquals(existingId, result);
        assertEquals(updatedName, spyWrappingPaper.getName());
        assertEquals(updatedStock, spyWrappingPaper.getStock());
        assertEquals(updatedImagePath, spyWrappingPaper.getImagePath());
        assertEquals(0, updatedPrice.compareTo(spyWrappingPaper.getPrice()));

        verify(objectStorageService).uploadObjects(List.of(mockMultipartFile));
        verify(objectStorageService).getUrl("uploaded/file/name");
    }

    @DisplayName("포장지 수정: 이미지 미포함")
    @Test
    void modifyWrappingPaper_without_image() {
        //given
        long existingId = existingWrappingPaper.getId();
        String updatedName = "updatedName";
        BigDecimal updatedPrice = BigDecimal.valueOf(2_000);
        Long updatedStock = 29L;
        WrappingPaperUpdateRequestDto updateRequest = new WrappingPaperUpdateRequestDto(updatedName, updatedPrice, updatedStock,null);
        WrappingPaper spyWrappingPaper = spy(existingWrappingPaper);

        when(wrappingPaperRepository.findById(existingId)).thenReturn(Optional.of(spyWrappingPaper));

        //when
        Long result = wrappingPaperService.modifyWrappingPaper(existingId, updateRequest);

        //then
        assertNotNull(result);
        assertEquals(existingId, result);
        assertEquals(updatedName, spyWrappingPaper.getName());
        assertEquals(updatedStock, spyWrappingPaper.getStock());
        assertEquals("/image/path", spyWrappingPaper.getImagePath());
        assertEquals(0, updatedPrice.compareTo(spyWrappingPaper.getPrice()));

        verify(objectStorageService, never()).uploadObjects(any(List.class));
        verify(objectStorageService, never()).getUrl(anyString());
    }

    @DisplayName("포장지 삭제")
    @Test
    void removeWrappingPaper() {
        //given
        long existingId = existingWrappingPaper.getId();

        when(wrappingPaperRepository.existsById(existingId)).thenReturn(true);

        //when
        wrappingPaperService.removeWrappingPaper(existingId);

        //then
        verify(wrappingPaperRepository).deleteById(existingId);
    }

    @DisplayName("포장지 삭제: 포장지 없음")
    @Test
    void removeWrappingPaper_wrappingPaper_not_found() {
        //given
        long existingId = existingWrappingPaper.getId();

        when(wrappingPaperRepository.existsById(existingId)).thenReturn(false);

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> wrappingPaperService.removeWrappingPaper(existingId));

        //then
        assertEquals(WRAPPING_PAPER_NOT_FOUND_MSG + existingId, exception.getMessage());
        verify(wrappingPaperRepository, never()).deleteById(existingId);
    }

    @DisplayName("포장지 재고 감소")
    @Test
    void reduceStock() {
        long existingId = existingWrappingPaper.getId();
        Long originalStock = existingWrappingPaper.getStock();
        int quantity = 3;

        when(wrappingPaperRepository.findById(existingId)).thenReturn(Optional.of(existingWrappingPaper));

        //when
        wrappingPaperService.reduceStock(existingId, quantity);

        //then
        assertEquals(originalStock - quantity, existingWrappingPaper.getStock());
    }

    @DisplayName("포장지 재고 감소: 포장지 못찾음")
    @Test
    void reduceStock_wrapping_paper_not_found() {
        long existingId = existingWrappingPaper.getId();
        int quantity = 3;

        when(wrappingPaperRepository.findById(existingId)).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> wrappingPaperService.reduceStock(existingId, quantity));

        //then
        assertEquals(WRAPPING_PAPER_NOT_FOUND_MSG + existingId, exception.getMessage());
    }
}