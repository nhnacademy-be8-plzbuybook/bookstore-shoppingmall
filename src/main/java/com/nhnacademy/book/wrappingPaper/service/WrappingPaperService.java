package com.nhnacademy.book.wrappingPaper.service;

import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.wrappingPaper.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface WrappingPaperService {
    WrappingPaperDto getWrappingPaper(long id);
    List<WrappingPaperDto> getWrappingPapers();
    Long createWrappingPaper(WrappingCreateSaveRequestDto saveRequest);
    Long modifyWrappingPaper(long id, WrappingPaperUpdateRequestDto updateRequest);
    void removeWrappingPaper(long id);
}

