package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.dto.request.BookRegisterDto;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;


//    @Test
//    void registerBook() {
//
//        BookRegisterDto registerDto = new BookRegisterDto();
//        registerDto.setBookDescription("test book description");
//        registerDto.setBookTitle("test book title");
//        registerDto.setBookIndex("test book index");
//        registerDto.setBookIsbn13("test book isbn 13");
//    }


}
