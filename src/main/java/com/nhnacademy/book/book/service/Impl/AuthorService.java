package com.nhnacademy.book.book.service.Impl;


import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.exception.AuthorNameNotFoundException;
import com.nhnacademy.book.book.exception.AuthorsNotFoundException;
import com.nhnacademy.book.book.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    public List<Author> getAllAuthors() {

        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {

        if(authorRepository.findById(id).isEmpty()) {
            throw new AuthorsNotFoundException("Author not Found");
        }

        return authorRepository.findById(id).get();
    }

    public Author createAuthor(Author author) {

        if(author.getAuthorName() == null || author.getAuthorName().isEmpty()) {
            throw new AuthorNameNotFoundException("Author name is Empty");
        }

        return authorRepository.save(author);
    }

    public void deleteAuthorById(Long id) {
        if(authorRepository.findById(id).isEmpty()) {
            throw new AuthorsNotFoundException("Author not Found");
        }
        authorRepository.deleteById(id);
    }
}
