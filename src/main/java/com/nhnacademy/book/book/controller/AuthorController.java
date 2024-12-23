package com.nhnacademy.book.book.controller;


import com.nhnacademy.book.book.dto.request.AuthorRequestDto;
import com.nhnacademy.book.book.dto.response.AuthorResponseDto;
import com.nhnacademy.book.book.service.Impl.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    @Autowired
    private final AuthorService authorService;


    @PostMapping("/authors")
    public ResponseEntity<Void> createAuthor(@RequestBody AuthorRequestDto authorRequestDto) {
        authorService.createAuthor(authorRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/authors/{authorId}")
    public ResponseEntity<AuthorResponseDto> getAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorResponseDto>> getAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @DeleteMapping("/authors/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.deleteAuthorById(authorId);
        return ResponseEntity.ok().build();
    }

}
