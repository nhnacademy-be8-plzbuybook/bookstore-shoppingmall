package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.dto.request.PublisherRegisterDto;
import com.nhnacademy.book.book.dto.request.PublisherRequestDto;
import com.nhnacademy.book.book.dto.response.PublisherResponseDto;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.exception.PublisherNotFoundException;
import com.nhnacademy.book.book.repository.PublisherRepository;
import com.nhnacademy.book.book.service.Impl.PublisherService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

    @InjectMocks
    private PublisherService publisherService;

    @Mock
    private PublisherRepository publisherRepository;

    // 테스트에서 사용할 Publisher 객체
    private Publisher publisher = new Publisher();
    @Disabled
    @Test
    void createPublisher() {
        PublisherRegisterDto publisherRegisterDto = new PublisherRegisterDto();
        publisherRegisterDto.setPublisherName("test");

        Publisher publisher = new Publisher();
        publisher.setPublisherName("test");

        Mockito.when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

        PublisherResponseDto createdPublisher = publisherService.createPublisher(publisherRegisterDto);

        Mockito.verify(publisherRepository, Mockito.times(1)).save(any(Publisher.class));
        assertEquals("test", createdPublisher.getPublisherName());
    }

    @Test
    void createPublisher_PublisherNotFound() {
        PublisherRegisterDto publisherRegisterDto = new PublisherRegisterDto();
        publisherRegisterDto.setPublisherName(""); // 빈 값 설정

        assertThrows(PublisherNotFoundException.class, () -> publisherService.createPublisher(publisherRegisterDto));
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

        publisherService.deletePublisher(1L);

        Mockito.verify(publisherRepository, Mockito.times(1)).delete(publisher);
    }

    @Test
    void deletePublisher_PublisherNotFound_PublisherIdIsNull() {
        PublisherRequestDto publisherRequestDto = new PublisherRequestDto();
        publisherRequestDto.setPublisherId(null);
        publisherRequestDto.setPublisherName("test");
        Publisher publisher = new Publisher();
        Mockito.when(publisherRepository.findById(any())).thenReturn(Optional.of(publisher));

        assertThrows(PublisherNotFoundException.class, () -> publisherService.deletePublisher(1L));}


    @Test
    void deletePublisher_PublisherNotFound_PublisherNameIsEmpty() {
        PublisherRequestDto publisherRequestDto = new PublisherRequestDto();


        Publisher publisher = new Publisher();
        publisher.setPublisherName("");
        publisherRequestDto.setPublisherId(1L);
        publisherRequestDto.setPublisherName("");

        Mockito.when(publisherRepository.findById(any())).thenReturn(Optional.of(publisher));

        assertThrows(PublisherNotFoundException.class, () -> publisherService.deletePublisher(1L));
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
        assertThrows(PublisherNotFoundException.class, () -> publisherService.findPublisherById(1L));
    }
}
