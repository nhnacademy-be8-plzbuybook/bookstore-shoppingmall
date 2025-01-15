package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.BookAuthorRequestDto;
import com.nhnacademy.book.book.dto.response.BookAuthorResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.dto.response.AuthorResponseDto;
import com.nhnacademy.book.book.dto.response.SellingBookResponseDto;
import com.nhnacademy.book.book.elastic.document.BookAuthorDocument;
import com.nhnacademy.book.book.elastic.document.BookDocument;
import com.nhnacademy.book.book.elastic.repository.AuthorSearchRepository;
import com.nhnacademy.book.book.elastic.repository.BookAuthorSearchRepository;
import com.nhnacademy.book.book.elastic.repository.BookSearchRepository;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.exception.AuthorIdNotFoundException;
import com.nhnacademy.book.book.exception.BookAuthorNotFoundException;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookAuthorService {

    private final BookAuthorRepository bookAuthorRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AuthorSearchRepository authorSearchRepository;
    private final BookSearchRepository bookSearchRepository;
    private final BookAuthorSearchRepository bookAuthorSearchRepository;
    private final SellingBookRepository sellingBookRepository;
    private final BookImageRepository bookImageRepository; // 누락된 Repository 추가



    @Autowired
    public BookAuthorService(BookAuthorRepository bookAuthorRepository, BookRepository bookRepository, AuthorRepository authorRepository,
                             AuthorSearchRepository authorSearchRepository, BookSearchRepository bookSearchRepository,
                             BookAuthorSearchRepository bookAuthorSearchRepository,
                             SellingBookRepository sellingBookRepository, BookImageRepository bookImageRepository) {
        this.bookAuthorRepository = bookAuthorRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.authorSearchRepository = authorSearchRepository;
        this.bookSearchRepository = bookSearchRepository;
        this.bookAuthorSearchRepository = bookAuthorSearchRepository;
        this.sellingBookRepository = sellingBookRepository;
        this.bookImageRepository = bookImageRepository;
    }



    public void createBookAuthor(BookAuthorRequestDto bookAuthorRequestDto) {
        if (Objects.isNull(bookAuthorRequestDto.getAuthorId())) {
            throw new AuthorIdNotFoundException("Author not found");
        } else if (Objects.isNull(bookAuthorRequestDto.getBookId())) {
            throw new BookNotFoundException("Book not found");
        }

        Book book = bookRepository.findById(bookAuthorRequestDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        // authorRequestDto에서 Id로 Author를 조회
        Author author = authorRepository.findById(bookAuthorRequestDto.getAuthorId())
                .orElseThrow(() -> new AuthorIdNotFoundException("Author not found"));
        BookAuthor bookAuthor = new BookAuthor(book, author);
        bookAuthor = bookAuthorRepository.save(bookAuthor);

        new BookAuthorResponseDto(
                bookAuthor.getId(),
                bookAuthor.getBook().getBookId(),
                bookAuthor.getBook().getBookTitle(),
                bookAuthor.getAuthor().getAuthorId(),
                bookAuthor.getAuthor().getAuthorName()
        );
    }

    public void deleteBookAuthor(Long bookAuthorId) {
        if (!bookAuthorRepository.existsById(bookAuthorId)) {
            throw new BookAuthorNotFoundException("BookAuthor not found");
        }
        bookAuthorRepository.deleteById(bookAuthorId);
        bookAuthorSearchRepository.deleteById(bookAuthorId);
    }

    public List<BookResponseDto> findBooksByAuthorId(Long authorId) {
        List<Book> books = bookAuthorRepository.findBooksByAuthorId(authorId);

        if (!authorRepository.existsById(authorId)) {
            throw new AuthorIdNotFoundException("Author id: " + authorId + " not found");
        }

        return books.stream()
                .map(book -> new BookResponseDto(
                        book.getBookId(),
                        book.getBookTitle(),
                        book.getBookPriceStandard(),  // 가격
                        book.getBookIsbn13() // ISBN13
                ))
                .collect(Collectors.toList());
    }




    public List<AuthorResponseDto> findAuthorsByBookId(Long bookId) {
        List<Author> authors = bookAuthorRepository.findAuthorsByBookId(bookId);

        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Book id: " + bookId + " not found");
        }

        return authors.stream()
                .map(author -> new AuthorResponseDto(author.getAuthorId(), author.getAuthorName()))
                .collect(Collectors.toList());
    }




}
