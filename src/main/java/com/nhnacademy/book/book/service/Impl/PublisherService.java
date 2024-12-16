package com.nhnacademy.book.book.service.Impl;


import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.exception.PublisherNotFound;
import com.nhnacademy.book.book.repository.PublisherRepository;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher createPublisher(Publisher publisher) {
        if(publisher.getPublisherName().isEmpty()){
            throw new PublisherNotFound("publisher name is empty");
        }

        return publisherRepository.save(publisher);
    }

    public void deletePublisher(Publisher publisher) {
        if(publisher.getPublisherId() == null || publisher.getPublisherName().isEmpty()){
            throw new PublisherNotFound("publisher is empty");
        }

        publisherRepository.delete(publisher);
    }

    public Publisher getPublisher(Long publisherId) {

        if(publisherRepository.findById(publisherId).isEmpty()){
            throw new PublisherNotFound("publisher name not found");
        }

        return publisherRepository.findById(publisherId).get();
    }
}
