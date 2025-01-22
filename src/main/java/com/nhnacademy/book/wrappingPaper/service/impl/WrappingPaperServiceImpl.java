package com.nhnacademy.book.wrappingPaper.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.objectstorage.service.ObjectStorageService;
import com.nhnacademy.book.wrappingPaper.dto.WrappingCreateSaveRequestDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperUpdateRequestDto;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import com.nhnacademy.book.wrappingPaper.repository.WrappingPaperRepository;
import com.nhnacademy.book.wrappingPaper.service.WrappingPaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WrappingPaperServiceImpl implements WrappingPaperService {
    private final WrappingPaperRepository wrappingPaperRepository;
    private final ObjectStorageService objectStorageService;

    @Transactional(readOnly = true)
    @Override
    public WrappingPaperDto getWrappingPaper(long id) {
        WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id).orElseThrow(
                () -> new NotFoundException("포장지를 찾을 수 없습니다. 포장지 아이디: " + id)
        );
        return new WrappingPaperDto(wrappingPaper);
    }

    @Transactional(readOnly = true)
    @Override
    public List<WrappingPaperDto> getWrappingPapers() {
        List<WrappingPaper> wrappingPapers = wrappingPaperRepository.findAll();

        if (wrappingPapers.isEmpty()) {
            return null;
        }
        return wrappingPapers.stream().map(WrappingPaperDto::new).toList();
    }

    @Override
    public Long createWrappingPaper(WrappingCreateSaveRequestDto saveRequest) {
        if (isWrappingPaperExists(saveRequest.getName())) {
            throw new ConflictException(saveRequest.getName() + "는 이미 존재하는 포장지입니다.");
        }
        String imagePath = uploadWrappingPaperImage(saveRequest.getImageFile());
        WrappingPaper wrappingPaper = saveRequest.toEntity(imagePath);
        WrappingPaper savedWrappingPaper = wrappingPaperRepository.save(wrappingPaper);

        return savedWrappingPaper.getId();
    }

    @Transactional
    @Override
    public Long modifyWrappingPaper(long id, WrappingPaperUpdateRequestDto updateRequest) {
        WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id).orElseThrow(()
                -> new NotFoundException("찾을 수 없는 포장지입니다. 포장지 아이디: " + id));
        // 이미지 파일이 없으면 파일 제외하고 수정
        if (updateRequest.imageFile() == null) {
            wrappingPaper.update(updateRequest.name(), updateRequest.price(), updateRequest.stock());

        }
        // 이미지 파일이 있으면 파일 업로드하고 포함해서 수정
        else {
            String imagePath = uploadWrappingPaperImage(updateRequest.imageFile());
            wrappingPaper.update(updateRequest.name(), updateRequest.price(), updateRequest.stock(), imagePath);
        }
        return id;
    }

    @Override
    public void removeWrappingPaper(long id) {
        if (!wrappingPaperRepository.existsById(id)) {
            throw new NotFoundException("찾을 수 없는 포장지입니다. 포장지 아이디: " + id);
        }
        wrappingPaperRepository.deleteById(id);
    }

    @Override
    public void reduceStock(Long wrappingPaperId, int quantity) {
        WrappingPaper wrappingPaper = wrappingPaperRepository.findById(wrappingPaperId).orElseThrow(() -> new NotFoundException("찾을 수 없는 포장지입니다. 포장지 아이디: " + wrappingPaperId));
        wrappingPaper.setStock(wrappingPaper.getStock() - quantity);
    }

    private boolean isWrappingPaperExists(String name) {
        return wrappingPaperRepository.existsByName(name);
    }

    private String uploadWrappingPaperImage(MultipartFile imageFile) {
        String uploadedFileName = objectStorageService.uploadObjects(List.of(imageFile)).getFirst();
        return objectStorageService.getUrl(uploadedFileName);
    }

}
