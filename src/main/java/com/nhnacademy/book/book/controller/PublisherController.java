package com.nhnacademy.book.book.controller;


import com.nhnacademy.book.book.dto.request.PublisherRegisterDto;
import com.nhnacademy.book.book.dto.request.PublisherRequestDto;
import com.nhnacademy.book.book.dto.response.PublisherResponseDto;
import com.nhnacademy.book.book.service.Impl.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishers")
public class PublisherController {

    @Autowired
    private final  PublisherService publisherService;

    @PostMapping
    public ResponseEntity<Void> createPublisher(@RequestBody PublisherRegisterDto publisherRegisterDto) {
        publisherService.createPublisher(publisherRegisterDto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{publisherId}")
    public ResponseEntity<Void> deletePublisherById(@PathVariable Long publisherId) {
        publisherService.deletePublisher(publisherId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{publisherId}")
    public ResponseEntity<PublisherResponseDto> getPublisher(@PathVariable Long publisherId) {
        return ResponseEntity.ok(publisherService.findPublisherById(publisherId));
    }

    @GetMapping("/search/{publisherId}")
    public ResponseEntity<PublisherResponseDto> searchPublisher(@PathVariable Long publisherId) {
        return ResponseEntity.ok(publisherService.getPublisherFromElastic(publisherId));
    }


}
