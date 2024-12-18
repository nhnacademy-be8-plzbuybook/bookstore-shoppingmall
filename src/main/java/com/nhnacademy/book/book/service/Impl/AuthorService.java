package com.nhnacademy.book.book.service.Impl;


import com.nhnacademy.book.book.dto.request.AuthorRequestDto;
import com.nhnacademy.book.book.dto.response.AuthorResponseDto;
import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.exception.AuthorNameNotFoundException;
import com.nhnacademy.book.book.exception.AuthorsNotFoundException;
import com.nhnacademy.book.book.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    public List<AuthorResponseDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            throw new AuthorsNotFoundException("No authors found");
        }
        return authors.stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    public AuthorResponseDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorsNotFoundException("Author not found"));
        return toResponseDto(author);
    }

    public AuthorResponseDto createAuthor(AuthorRequestDto requestDto) {
        if (requestDto.getAuthorName() == null || requestDto.getAuthorName().isEmpty()) {
            throw new AuthorNameNotFoundException("Author name is empty");
        }

        Author author = new Author();
        author.setAuthorName(requestDto.getAuthorName());

        Author savedAuthor = authorRepository.save(author);
        return toResponseDto(savedAuthor);
    }

    public void deleteAuthorById(Long id) {
        if (authorRepository.findById(id).isEmpty()) {
            throw new AuthorsNotFoundException("Author not found");
        }
        authorRepository.deleteById(id);
    }


    private AuthorResponseDto toResponseDto(Author author) {
        return new AuthorResponseDto(
                author.getAuthorId(),
                author.getAuthorName()

        );
    }
}

