package com.nhnacademy.book.booktest;

import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AuthorTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void testCreateAuthor() {
        Author author = new Author();
        author.setAuthorName("test author");
        Author jpa = authorRepository.save(author);

        assertThat(jpa.getAuthorId()).isEqualTo(author.getAuthorId());
        assertThat(jpa.getAuthorName()).isEqualTo(author.getAuthorName());

    }
}
