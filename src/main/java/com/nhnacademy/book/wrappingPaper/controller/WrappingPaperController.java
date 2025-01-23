package com.nhnacademy.book.wrappingPaper.controller;

import com.nhnacademy.book.member.domain.dto.ErrorResponseDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingCreateSaveRequestDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperUpdateRequestDto;
import com.nhnacademy.book.wrappingPaper.service.WrappingPaperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/wrapping-papers")
@RestController
public class WrappingPaperController {
    private final WrappingPaperService wrappingPaperService;

    /**
     * 포장지 단건조회
     *
     * @param id 포장지 아이디
     * @return 포장지 DTO
     */
    @GetMapping("/{wrapping-paper-id}")
    public ResponseEntity<WrappingPaperDto> getWrappingPaper(@PathVariable("wrapping-paper-id") Long id) {
        WrappingPaperDto wrappingPaper = wrappingPaperService.getWrappingPaper(id);
        return ResponseEntity.status(HttpStatus.OK).body(wrappingPaper);
    }

    /**
     * 포장지 목록조회
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<WrappingPaperDto>> getWrappingPapers() {
        List<WrappingPaperDto> wrappingPapers = wrappingPaperService.getWrappingPapers();
        return ResponseEntity.status(HttpStatus.OK).body(wrappingPapers);
    }

    /**
     * 포장지 생성
     *
     * @param createRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<Long> createWrappingPaper(@Valid @ModelAttribute WrappingCreateSaveRequestDto createRequest) {
        Long createdWrappingPaperId = wrappingPaperService.createWrappingPaper(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWrappingPaperId);
    }

    /**
     * 포장지 수정
     *
     * @param id
     * @param updateRequest
     * @return
     */
    @PutMapping("/{wrapping-paper-id}")
    public ResponseEntity<Long> modifyWrappingPaper(@PathVariable("wrapping-paper-id") Long id,
                                                    @Valid @ModelAttribute WrappingPaperUpdateRequestDto updateRequest) {
        Long modifiedWrappingPaperId = wrappingPaperService.modifyWrappingPaper(id, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(modifiedWrappingPaperId);
    }

    /**
     * 포장지 삭제
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{wrapping-paper-id}")
    public ResponseEntity<Void> removeWrappingPaper(@PathVariable("wrapping-paper-id") Long id) {
        wrappingPaperService.removeWrappingPaper(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleBadRequest(MethodArgumentNotValidException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "포장지: 잘못된 입력입니다."
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
}
