package com.nhnacademy.book.book.service.Impl;


import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.exception.AuthorIdNotFoundException;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.repository.AuthorRepository;
import com.nhnacademy.book.book.repository.BookAuthorRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookAuthorService {

    private final BookAuthorRepository bookAuthorRepository;


    public BookAuthorService(BookAuthorRepository bookAuthorRepository) {
        this.bookAuthorRepository = bookAuthorRepository;
    }

    public List<Book> findBooksByAuthorId(Long authorId) {
        if(bookAuthorRepository.findBooksByAuthorId(authorId).isEmpty()) {
            throw new AuthorIdNotFoundException("Author id: " + authorId + " not found");
        }

        return bookAuthorRepository.findBooksByAuthorId(authorId);
    }

    public List<Author> findAuthorsByBookId(Long bookId) {

        if(bookAuthorRepository.findAuthorsByBookId(bookId).isEmpty()) {
            throw new BookNotFoundException("Book id: " + bookId + " not found");
        }
        return bookAuthorRepository.findAuthorsByBookId(bookId);
    }

}
