package com.nhnacademy.book.book.service.Impl;


import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookAuthor;
import com.nhnacademy.book.book.exception.AuthorIdNotFoundException;
import com.nhnacademy.book.book.exception.BookAuthorNotFoundException;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.repository.AuthorRepository;
import com.nhnacademy.book.book.repository.BookAuthorRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BookAuthorService {

    private final BookAuthorRepository bookAuthorRepository;




    public BookAuthorService(BookAuthorRepository bookAuthorRepository) {
        this.bookAuthorRepository = bookAuthorRepository;
    }


    public BookAuthor createBookAuthor(Book book, Author author) {
        if(Objects.isNull(author)) {
            throw new AuthorIdNotFoundException("Author not found");
        } else if(Objects.isNull(book)) {
            throw new BookNotFoundException("Book not found");
        }

        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBook(book);
        bookAuthor.setAuthor(author);
        bookAuthor.setAuthor(author);
        return bookAuthorRepository.save(bookAuthor);
    }

    public void deleteBookAuthor(BookAuthor bookAuthor) {
        if(Objects.isNull(bookAuthor)) {
            throw new BookAuthorNotFoundException("BookAuthor not found");
        }
        bookAuthorRepository.delete(bookAuthor);
    }


    public List<Book> findBooksByAuthorId(Long authorId) {
        List<Book> books = bookAuthorRepository.findBooksByAuthorId(authorId);

        if (books == null || books.isEmpty()) {  // null 체크 및 빈 리스트 확인
            throw new AuthorIdNotFoundException("Author id: " + authorId + " not found");
        }

        return books;
    }

    public List<Author> findAuthorsByBookId(Long bookId) {

        List<Author> authors = bookAuthorRepository.findAuthorsByBookId(bookId);
        if(authors == null || authors.isEmpty()) {
            throw new BookNotFoundException("Book id: " + bookId + " not found");
        }
        return authors;
    }

}
