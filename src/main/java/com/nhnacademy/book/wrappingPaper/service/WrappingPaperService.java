package com.nhnacademy.book.wrappingPaper.service;

import com.nhnacademy.book.wrappingPaper.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WrappingPaperService {
    WrappingPaperDto getWrappingPaper(long id);
    List<WrappingPaperDto> getWrappingPapers();
    WrappingPaperSaveResponseDto createWrappingPaper(WrappingPaperSaveRequestDto saveRequest, MultipartFile imageFile);
    WrappingPaperUpdateResponseDto modifyWrappingPaper(long id, WrappingPaperUpdateRequestDto updateRequest, MultipartFile imageFile);
    void removeWrappingPaper(long id);

}
