package com.nhnacademy.book.wrappingPaper.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.member.domain.Image;
import com.nhnacademy.book.wrappingPaper.dto.*;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import com.nhnacademy.book.wrappingPaper.repository.WrappingPaperRepository;
import com.nhnacademy.book.wrappingPaper.service.WrappingPaperService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WrappingPaperServiceImpl implements WrappingPaperService {
    private final WrappingPaperRepository wrappingPaperRepository;

    @Override
    public WrappingPaperDto getWrappingPaper(long id) {
        Optional<WrappingPaper> wrappingPaper = wrappingPaperRepository.findById(id);

        if (wrappingPaper.isEmpty()) {
            throw new NotFoundException(id + "wrapping paper not found!");
        }
        return new WrappingPaperDto(wrappingPaper.get());
    }

    @Override
    public List<WrappingPaperDto> getWrappingPapers() {
        List<WrappingPaper> wrappingPapers = wrappingPaperRepository.findAll();

        if (wrappingPapers.isEmpty()) {
            return null;
        }
        return wrappingPapers.stream().map(WrappingPaperDto::new).toList();
    }

    @Override
    public WrappingPaperSaveResponseDto createWrappingPaper(WrappingPaperSaveRequestDto saveRequest, @NotNull MultipartFile imageFile) {
        if (wrappingPaperRepository.existsByName(saveRequest.getName())) {
            throw new ConflictException("wrapping paper: [" + saveRequest.getName() + "] is already exists!");
        }
        // TODO: 이미지 저장 메서드 호출 / imageFile 업로드
        String image = "image/path";
        WrappingPaper wrappingPaper = saveRequest.toEntity(image);
        WrappingPaper savedWrappingPaper = wrappingPaperRepository.save(wrappingPaper);

        return new WrappingPaperSaveResponseDto(savedWrappingPaper.getId());
    }

    @Override
    public WrappingPaperUpdateResponseDto modifyWrappingPaper(long id, WrappingPaperUpdateRequestDto updateRequest, @Nullable MultipartFile imageFile) {
        Optional<WrappingPaper> wrappingPaper = wrappingPaperRepository.findById(id);

        if (wrappingPaper.isEmpty()) {
            throw new NotFoundException(id + "wrapping paper not found!");
        }
        WrappingPaper target = wrappingPaper.get();

        if (imageFile == null) {
            target.update(updateRequest.name(), updateRequest.price(), updateRequest.stock(), updateRequest.imagePath());

        } else {
            // TODO: 이미지 저장 메서드 호출 / imageFile 업로드
            String image = "/update/path";
            target.update(updateRequest.name(), updateRequest.price(), updateRequest.stock(), image);
        }
        return new WrappingPaperUpdateResponseDto(id);
    }

    @Override
    public void removeWrappingPaper(long id) {
        if (wrappingPaperRepository.existsById(id)) {
            throw new NotFoundException(id + "wrapping paper not found!");
        }
        wrappingPaperRepository.deleteById(id);
    }
}
