package com.nhnacademy.book.booktest.service;


import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.repository.BookAuthorRepository;
import com.nhnacademy.book.book.service.BookService;
import com.nhnacademy.book.book.service.Impl.AuthorService;
import com.nhnacademy.book.book.service.Impl.BookAuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookAuthorServiceTest {

    @InjectMocks
    private BookAuthorService bookAuthorService;

    @Mock
    private BookAuthorRepository bookAuthorRepository;


    @Test
    void createBookAuthor() {



    }
}
