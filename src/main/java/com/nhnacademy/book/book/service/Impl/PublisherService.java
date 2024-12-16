package com.nhnacademy.book.book.service.Impl;


import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.exception.PublisherNotFound;
import com.nhnacademy.book.book.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public Publisher findPublisherById(Long publisherId) {
        return publisherRepository.findById(publisherId)
                .orElseThrow(() -> new PublisherNotFound("publisher not found"));

    }
}
