package com.nhnacademy.book.book.controller;


import com.nhnacademy.book.book.dto.request.AuthorRequestDto;
import com.nhnacademy.book.book.dto.response.AuthorResponseDto;
import com.nhnacademy.book.book.elastic.repository.AuthorSearchRepository;
import com.nhnacademy.book.book.service.Impl.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthorController {

    @Autowired
    private final AuthorService authorService;
    @Autowired
    private AuthorSearchRepository authorSearchRepository;


    @PostMapping("/authors")
    public ResponseEntity<Void> createAuthor(@RequestBody AuthorRequestDto authorRequestDto) {
        authorService.createAuthor(authorRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/authors")
    public ResponseEntity<Page<AuthorResponseDto>> getAllAuthors(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AuthorResponseDto> authors;

        if (keyword != null && !keyword.isEmpty()) {
            authors = authorService.searchAuthorsByKeyword(keyword, pageable);
        } else {
            authors = authorService.findAll(pageable);
        }

        return ResponseEntity.ok(authors);
    }

    @GetMapping("{authorId}")
    public ResponseEntity<AuthorResponseDto> getAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }


//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Page<AuthorResponseDto>> getAllAuthors(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
//    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.deleteAuthorById(authorId);
        authorSearchRepository.deleteById(authorId);
        return ResponseEntity.ok().build();
    }


}
