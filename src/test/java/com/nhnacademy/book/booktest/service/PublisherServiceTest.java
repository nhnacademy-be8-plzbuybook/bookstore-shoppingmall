package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.exception.AuthorNameNotFoundException;
import com.nhnacademy.book.book.exception.PublisherNotFound;
import com.nhnacademy.book.book.repository.AuthorRepository;
import com.nhnacademy.book.book.repository.PublisherRepository;
import com.nhnacademy.book.book.service.Impl.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {


    @InjectMocks
    private PublisherService publisherService;

    @Mock
    private PublisherRepository publisherRepository;

    private Publisher publisher;


    @Test
    void createPublisher(){

        Publisher publisher = new Publisher();
        publisher.setPublisherName("test");

        Mockito.when(publisherRepository.save(Mockito.any(Publisher.class))).thenReturn(publisher);
        Publisher createdPublisher = publisherService.createPublisher(publisher);


        Mockito.verify(publisherRepository, Mockito.times(1)).save(Mockito.any(Publisher.class));
        assertEquals(createdPublisher.getPublisherName(),"test");

    }

    @Test
    void createPublisher_PublisherNotFound(){
        Publisher publisher = new Publisher();
        publisher.setPublisherName("");
//        Mockito.when(publisherRepository.save(Mockito.any(Publisher.class))).thenReturn(publisher);

        assertThrows(PublisherNotFound.class, () -> publisherService.createPublisher(publisher));

    }

    @Test
    void deletePublisher(){
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setPublisherName("Valid Publisher");

        publisherService.deletePublisher(publisher);

        Mockito.verify(publisherRepository, Mockito.times(1)).delete(publisher);

    }

    @Test
    void deletePublisher_PublisherNotFound(){

        Publisher publisher = new Publisher();
        publisher.setPublisherId(null);
        publisher.setPublisherName("test");
        assertThrows(PublisherNotFound.class, () -> publisherService.deletePublisher(publisher));
        publisher.setPublisherId(1L);
        publisher.setPublisherName("");
        assertThrows(PublisherNotFound.class, () -> publisherService.deletePublisher(publisher));

    }


    @Test
    void findPublisherById(){
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setPublisherName("test");
        Mockito.when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));

        Publisher result = publisherService.findPublisherById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getPublisherId());
        assertEquals("test", result.getPublisherName());

        Mockito.verify(publisherRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void findPublisherById_PublisherNotFound(){
        Publisher publisher = new Publisher();
        publisher.setPublisherId(null);
        publisher.setPublisherName("test");
        assertThrows(PublisherNotFound.class, () -> publisherService.findPublisherById(1L));

    }

}
