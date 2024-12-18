package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.dto.request.PublisherRequestDto;
import com.nhnacademy.book.book.dto.response.PublisherResponseDto;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.exception.PublisherNotFound;
import com.nhnacademy.book.book.repository.PublisherRepository;
import com.nhnacademy.book.book.service.Impl.PublisherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

    @InjectMocks
    private PublisherService publisherService;

    @Mock
    private PublisherRepository publisherRepository;

    // 테스트에서 사용할 Publisher 객체
    private Publisher publisher = new Publisher();

    @Test
    void createPublisher() {
        PublisherRequestDto publisherRequestDto = new PublisherRequestDto();
        publisherRequestDto.setPublisherName("test");

        Publisher publisher = new Publisher();
        publisher.setPublisherName("test");

        Mockito.when(publisherRepository.save(Mockito.any(Publisher.class))).thenReturn(publisher);

        PublisherResponseDto createdPublisher = publisherService.createPublisher(publisherRequestDto);

        Mockito.verify(publisherRepository, Mockito.times(1)).save(Mockito.any(Publisher.class));
        assertEquals("test", createdPublisher.getPublisherName());
    }

    @Test
    void createPublisher_PublisherNotFound() {
        PublisherRequestDto publisherRequestDto = new PublisherRequestDto();
        publisherRequestDto.setPublisherName(""); // 빈 값 설정

        assertThrows(PublisherNotFound.class, () -> publisherService.createPublisher(publisherRequestDto));
    }

    @Test
    void deletePublisher() {
        PublisherRequestDto publisherRequestDto = new PublisherRequestDto();
        publisherRequestDto.setPublisherId(1L);
        publisherRequestDto.setPublisherName("Valid Publisher");

        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setPublisherName("Valid Publisher");

        Mockito.when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));

        publisherService.deletePublisher(publisherRequestDto);

        Mockito.verify(publisherRepository, Mockito.times(1)).delete(publisher);
    }

    @Test
    void deletePublisher_PublisherNotFound() {
        PublisherRequestDto publisherRequestDto = new PublisherRequestDto();
        publisherRequestDto.setPublisherId(null);
        publisherRequestDto.setPublisherName("test");

        assertThrows(PublisherNotFound.class, () -> publisherService.deletePublisher(publisherRequestDto));

        publisherRequestDto.setPublisherId(1L);
        publisherRequestDto.setPublisherName("");
        assertThrows(PublisherNotFound.class, () -> publisherService.deletePublisher(publisherRequestDto));
    }

    @Test
    void findPublisherById() {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setPublisherName("test");

        Mockito.when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));

        PublisherResponseDto result = publisherService.findPublisherById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getPublisherId());
        assertEquals("test", result.getPublisherName());

        Mockito.verify(publisherRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void findPublisherById_PublisherNotFound() {
        Mockito.when(publisherRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PublisherNotFound.class, () -> publisherService.findPublisherById(1L));
    }
}
