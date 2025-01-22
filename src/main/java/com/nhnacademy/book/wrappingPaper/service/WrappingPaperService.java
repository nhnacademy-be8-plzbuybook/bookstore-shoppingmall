package com.nhnacademy.book.wrappingPaper.service;

import com.nhnacademy.book.wrappingPaper.dto.WrappingCreateSaveRequestDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperUpdateRequestDto;

import java.util.List;

public interface WrappingPaperService {
    WrappingPaperDto getWrappingPaper(long id);
    List<WrappingPaperDto> getWrappingPapers();
    Long createWrappingPaper(WrappingCreateSaveRequestDto saveRequest);
    Long modifyWrappingPaper(long id, WrappingPaperUpdateRequestDto updateRequest);
    void removeWrappingPaper(long id);
    void reduceStock(Long wrappingPaperId, int quantity);
}

