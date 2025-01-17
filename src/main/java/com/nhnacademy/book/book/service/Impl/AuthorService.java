package com.nhnacademy.book.book.service.Impl;


import com.nhnacademy.book.book.dto.request.AuthorRequestDto;
import com.nhnacademy.book.book.dto.response.AuthorResponseDto;
import com.nhnacademy.book.book.elastic.document.AuthorDocument;
import com.nhnacademy.book.book.elastic.repository.AuthorSearchRepository;
import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.exception.AuthorNameNotFoundException;
import com.nhnacademy.book.book.exception.AuthorsNotFoundException;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AuthorService {

    @Autowired
    private final AuthorRepository authorRepository;
    private final AuthorSearchRepository authorSearchRepository;

    public AuthorService(AuthorRepository authorRepository, AuthorSearchRepository authorSearchRepository) {
        this.authorRepository = authorRepository;
        this.authorSearchRepository = authorSearchRepository;
    }

    public Page<AuthorResponseDto> getAllAuthors(Pageable pageable) {
        Page<Author> authors = authorRepository.findAll(pageable);
        return authors.map(this::toResponseDto);
    }


    public AuthorResponseDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorsNotFoundException("Author not found"));
        return toResponseDto(author);
    }

    public AuthorResponseDto getAuthorByNameFromElastic(String name) {

        AuthorDocument authorDocument = authorSearchRepository.findByAuthorName(name);

        if (authorDocument == null) {
            log.error("author not found");

            throw new AuthorNameNotFoundException("Author name not found");
        }

        return new AuthorResponseDto(
                authorDocument.getAuthorId(),
                authorDocument.getAuthorName()
        );

    }

    public AuthorResponseDto createAuthor(AuthorRequestDto requestDto) {
        if (requestDto.getAuthorName() == null || requestDto.getAuthorName().isEmpty()) {
            throw new AuthorNameNotFoundException("Author name is empty");
        }

        Author author = new Author();
        author.setAuthorName(requestDto.getAuthorName());

        Author savedAuthor = authorRepository.save(author);

        AuthorDocument authorDocument = new AuthorDocument();
        authorDocument.setAuthorId(savedAuthor.getAuthorId());
        authorDocument.setAuthorName(savedAuthor.getAuthorName());
//        authorSearchRepository.save(authorDocument);

        return toResponseDto(savedAuthor);
    }

    public void deleteAuthorById(Long id) {
        if (authorRepository.findById(id).isEmpty()) {
            throw new AuthorsNotFoundException("Author not found");
        }
        authorRepository.deleteById(id);
        authorSearchRepository.deleteByAuthorId(id);
    }


    private AuthorResponseDto toResponseDto(Author author) {
        return new AuthorResponseDto(
                author.getAuthorId(),
                author.getAuthorName()

        );
    }
}

