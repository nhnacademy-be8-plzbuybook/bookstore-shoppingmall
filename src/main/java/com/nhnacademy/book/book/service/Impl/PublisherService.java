package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.PublisherRequestDto;
import com.nhnacademy.book.book.dto.response.PublisherResponseDto;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.exception.PublisherNotFoundException;
import com.nhnacademy.book.book.repository.PublisherRepository;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    // Publisher 생성
    public PublisherResponseDto createPublisher(PublisherRequestDto publisherRequestDto) {
        // PublisherRequestDto로부터 Publisher 엔티티 생성
        if (publisherRequestDto.getPublisherName().isEmpty()) {
            throw new PublisherNotFoundException("Publisher name is empty");
        }

        Publisher publisher = new Publisher();
        publisher.setPublisherName(publisherRequestDto.getPublisherName());

        // 저장
        Publisher savedPublisher = publisherRepository.save(publisher);

        // PublisherResponseDto로 변환하여 반환
        return convertToDto(savedPublisher);
    }

    // Publisher 삭제
    public void deletePublisher(PublisherRequestDto publisherRequestDto) {
        Publisher publisher = publisherRepository.findById(publisherRequestDto.getPublisherId())
                .orElseThrow(() -> new PublisherNotFoundException("Publisher not found"));

        if (publisher.getPublisherId() == null || publisher.getPublisherName().isEmpty()) {
            throw new PublisherNotFoundException("Publisher is empty");
        }

        publisherRepository.delete(publisher);
    }

    // Publisher 조회
    public PublisherResponseDto findPublisherById(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new PublisherNotFoundException("Publisher not found"));

        // PublisherResponseDto로 변환하여 반환
        return convertToDto(publisher);
    }

    // Publisher -> PublisherResponseDto 변환
    private PublisherResponseDto convertToDto(Publisher publisher) {
        PublisherResponseDto responseDto = new PublisherResponseDto();
        responseDto.setPublisherId(publisher.getPublisherId());
        responseDto.setPublisherName(publisher.getPublisherName());
        return responseDto;
    }
}
