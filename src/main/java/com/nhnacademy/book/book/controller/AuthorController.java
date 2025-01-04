package com.nhnacademy.book.book.controller;


import com.nhnacademy.book.book.dto.request.AuthorRequestDto;
import com.nhnacademy.book.book.dto.response.AuthorResponseDto;
import com.nhnacademy.book.book.elastic.repository.AuthorSearchRepository;
import com.nhnacademy.book.book.service.Impl.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private final AuthorService authorService;
    @Autowired
    private AuthorSearchRepository authorSearchRepository;


    @PostMapping
    public ResponseEntity<Void> createAuthor(@RequestBody AuthorRequestDto authorRequestDto) {
        authorService.createAuthor(authorRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{authorId}")
    public ResponseEntity<AuthorResponseDto> getAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }



    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.deleteAuthorById(authorId);
        authorSearchRepository.deleteById(authorId);
        return ResponseEntity.ok().build();
    }


}
