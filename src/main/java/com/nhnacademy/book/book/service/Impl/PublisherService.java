package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.PublisherRegisterDto;
import com.nhnacademy.book.book.dto.response.PublisherResponseDto;
import com.nhnacademy.book.book.elastic.document.PublisherDocument;
import com.nhnacademy.book.book.elastic.repository.PublisherSearchRepository;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.exception.PublisherNotFoundException;
import com.nhnacademy.book.book.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherSearchRepository publisherSearchRepository;

    public PublisherService(PublisherRepository publisherRepository, PublisherSearchRepository publisherSearchRepository) {
        this.publisherRepository = publisherRepository;
        this.publisherSearchRepository = publisherSearchRepository;
    }

    // Publisher 생성
    public PublisherResponseDto createPublisher(PublisherRegisterDto publisherRegisterDto) {
        // PublisherRequestDto로부터 Publisher 엔티티 생성
        if (publisherRegisterDto.getPublisherName().isEmpty()) {
            throw new PublisherNotFoundException("Publisher name is empty");
        }

        Publisher publisher = new Publisher();
        publisher.setPublisherName(publisherRegisterDto.getPublisherName());



        // 저장
        Publisher savedPublisher = publisherRepository.save(publisher);

        PublisherDocument publisherDocument = new PublisherDocument();
        publisherDocument.setPublisherId(savedPublisher.getPublisherId());
        publisherDocument.setPublisherName(savedPublisher.getPublisherName());

        publisherSearchRepository.save(publisherDocument);


        // PublisherResponseDto로 변환하여 반환
        return convertToDto(savedPublisher);
    }


    // Publisher 삭제
    public void deletePublisher(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
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
