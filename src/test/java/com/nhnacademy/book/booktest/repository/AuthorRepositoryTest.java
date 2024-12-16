package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.repository.AuthorRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testCreateAuthor() {
        Author author = new Author();
        author.setAuthorName("test author");
        Author jpa = authorRepository.save(author);

        assertThat(jpa.getAuthorId()).isEqualTo(author.getAuthorId());
        assertThat(jpa.getAuthorName()).isEqualTo(author.getAuthorName());

    }

    @Test
    void deleteAuthor() {
        Author author = new Author();
        author.setAuthorName("test author");
        Author jpa = authorRepository.save(author);
        authorRepository.delete(author);

        Assertions.assertThat(authorRepository.findById(jpa.getAuthorId())).isEmpty();
    }

    @Test
    void updateAuthor() {
        Author author = new Author();
        author.setAuthorName("test author");
        Author jpa = authorRepository.save(author);
        author.setAuthorName("new author");
        authorRepository.save(author);
        Author updatedAuthor = authorRepository.findById(jpa.getAuthorId()).get();
        assertThat(updatedAuthor.getAuthorName()).isEqualTo("new author");
    }

}
