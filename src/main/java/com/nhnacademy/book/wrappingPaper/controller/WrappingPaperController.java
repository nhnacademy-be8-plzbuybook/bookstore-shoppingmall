package com.nhnacademy.book.wrappingPaper.controller;

import com.nhnacademy.book.wrappingPaper.dto.*;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import com.nhnacademy.book.wrappingPaper.service.WrappingPaperService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/wrapping-papers")
@RestController
public class WrappingPaperController {
    private final WrappingPaperService wrappingPaperService;

    @GetMapping("/{wrapping-paper-id}")
    public ResponseEntity<WrappingPaperDto> getWrappingPaper(@PathVariable("wrapping-paper-id") Long id) {
        WrappingPaperDto wrappingPaper = wrappingPaperService.getWrappingPaper(id);
        return ResponseEntity.status(HttpStatus.OK).body(wrappingPaper);
    }

    @GetMapping
    public ResponseEntity<List<WrappingPaperDto>> getWrappingPapers() {
        List<WrappingPaperDto> wrappingPapers = wrappingPaperService.getWrappingPapers();
        return ResponseEntity.status(HttpStatus.OK).body(wrappingPapers);
    }

    @PostMapping
    public ResponseEntity<WrappingPaperSaveResponseDto> createWrappingPaper(@Valid @RequestPart WrappingPaperSaveRequestDto saveRequest,
                                                                            @NotNull @RequestPart MultipartFile imageFile) {
        WrappingPaperSaveResponseDto saveResponse = wrappingPaperService.createWrappingPaper(saveRequest, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveResponse);
    }

    @PutMapping("/{wrapping-paper-id}")
    public ResponseEntity<WrappingPaperUpdateResponseDto> modifyWrappingPaper(@PathVariable("wrapping-paper-id") Long id,
                                                                              @Valid @RequestPart WrappingPaperUpdateRequestDto updateRequest,
                                                                              @Nullable @RequestPart MultipartFile imageFile) {
        WrappingPaperUpdateResponseDto updateResponse = wrappingPaperService.modifyWrappingPaper(id, updateRequest, imageFile);
        return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
    }

    @DeleteMapping("/{wrapping-paper-id}")
    public ResponseEntity<Void> removeWrappingPaper(@PathVariable("wrapping-paper-id") Long id) {
        wrappingPaperService.removeWrappingPaper(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
