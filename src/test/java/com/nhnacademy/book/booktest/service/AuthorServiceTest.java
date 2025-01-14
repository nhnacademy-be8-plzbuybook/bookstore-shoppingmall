package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.dto.request.AuthorRequestDto;
import com.nhnacademy.book.book.dto.response.AuthorResponseDto;
import com.nhnacademy.book.book.elastic.document.AuthorDocument;
import com.nhnacademy.book.book.elastic.repository.AuthorSearchRepository;
import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.exception.AuthorNameNotFoundException;
import com.nhnacademy.book.book.exception.AuthorsNotFoundException;
import com.nhnacademy.book.book.repository.AuthorRepository;
import com.nhnacademy.book.book.service.Impl.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @InjectMocks
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorSearchRepository authorSearchRepository;

    List<Author> authorList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Author author1 = new Author();
        author1.setAuthorId(1L);
        author1.setAuthorName("test1");

        Author author2 = new Author();
        author2.setAuthorId(2L);
        author2.setAuthorName("test2");

        Author author3 = new Author();
        author3.setAuthorId(3L);
        author3.setAuthorName("test3");

        Author author4 = new Author();
        author4.setAuthorId(4L);
        author4.setAuthorName("test4");

        authorList.add(author1);
        authorList.add(author2);
        authorList.add(author3);
        authorList.add(author4);
    }
    @Test
    void createAuthor() {
        AuthorRequestDto requestDto = new AuthorRequestDto("test_created");
        Author author = new Author();
        author.setAuthorId(1L);
        author.setAuthorName(requestDto.getAuthorName());

        Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);

        AuthorResponseDto responseDto = authorService.createAuthor(requestDto);
        assertThat(responseDto.getAuthorName()).isEqualTo("test_created");
    }

    @Test
    void createAuthor_NameNotFoundException() {
        AuthorRequestDto requestDto = new AuthorRequestDto(null);
        assertThrows(AuthorNameNotFoundException.class, () -> authorService.createAuthor(requestDto));
    }

    @Test
    void createAuthor_NameNotFoundException2() {
        AuthorRequestDto requestDto = new AuthorRequestDto("");
        assertThrows(AuthorNameNotFoundException.class, () -> authorService.createAuthor(requestDto));
    }

//    @Test
//    void getAllAuthors() {
//        Mockito.when(authorRepository.findAll()).thenReturn(authorList);
//
//        List<AuthorResponseDto> responseDtos = authorService.getAllAuthors();
//        assertThat(responseDtos.size()).isEqualTo(4);
//    }


    @Test
    void getAuthorById() {
        Author author = new Author();
        author.setAuthorId(5L);
        author.setAuthorName("test_created");

        Mockito.when(authorRepository.findById(5L)).thenReturn(Optional.of(author));

        AuthorResponseDto responseDto = authorService.getAuthorById(5L);
        assertThat(responseDto.getAuthorName()).isEqualTo("test_created");
    }

    @Test
    void getAuthorById_AuthorsNotFoundException() {
        Mockito.when(authorRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(AuthorsNotFoundException.class, () -> authorService.getAuthorById(5L));
    }

    @Test
    void deleteAuthorById() {
        Author author = new Author();
        author.setAuthorId(5L);
        author.setAuthorName("test_created");

        Mockito.when(authorRepository.findById(5L)).thenReturn(Optional.of(author));

        authorService.deleteAuthorById(5L);
        Mockito.verify(authorRepository, Mockito.times(1)).deleteById(5L);
    }

    @Test
    void deleteAuthorById_AuthorsNotFoundException() {
        Mockito.when(authorRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(AuthorsNotFoundException.class, () -> authorService.deleteAuthorById(5L));
    }

    @Test
    void getAuthorByNameFromElastic() {
        // Given
        String authorName = "test_author";
        AuthorDocument authorDocument = new AuthorDocument();
        authorDocument.setAuthorId(1L);
        authorDocument.setAuthorName(authorName);

        Mockito.when(authorSearchRepository.findByAuthorName(authorName)).thenReturn(authorDocument);

        // When
        AuthorResponseDto responseDto = authorService.getAuthorByNameFromElastic(authorName);

        // Then
        assertThat(responseDto.getAuthorId()).isEqualTo(1L);
        assertThat(responseDto.getAuthorName()).isEqualTo(authorName);
    }

    @Test
    void getAuthorByNameFromElastic_AuthorNameNotFoundException() {
        // Given
        String authorName = "non_existing_author";

        Mockito.when(authorSearchRepository.findByAuthorName(authorName)).thenReturn(null);

        // Then
        assertThrows(AuthorNameNotFoundException.class, () -> authorService.getAuthorByNameFromElastic(authorName));
    }

}
