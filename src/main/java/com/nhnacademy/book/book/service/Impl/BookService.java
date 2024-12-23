package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.*;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.PublisherNotFoundException;
import com.nhnacademy.book.book.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    //TODO 수정

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public BookService(BookRepository bookRepository,
                       PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<BookDetailResponseDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        return books.stream()
                .map(book -> new BookDetailResponseDto(
                        book.getBookId(),
                        book.getBookTitle(),
                        book.getBookIndex(),
                        book.getBookDescription(),
                        book.getBookPubDate(),
                        book.getBookPriceStandard(),
                        book.getBookIsbn13(),
                        book.getPublisher().getPublisherName() // publisherName을 DTO에 포함
                ))
                .collect(Collectors.toList());

    }

    // 도서 상세 조회 기능
    public BookDetailResponseDto getBookDetail(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("존재하지 않는 도서 ID입니다."));
        return new BookDetailResponseDto(
                book.getBookId(),
                book.getBookTitle(),
                book.getBookIndex(),
                book.getBookDescription(),
                book.getBookPubDate(),
                book.getBookPriceStandard(),
                book.getBookIsbn13(),
                book.getPublisher().getPublisherName());
    }

    // 도서 등록 기능 (관리자)
    public void registerBook(BookRegisterDto bookRegisterDto) {
        if(Objects.isNull(bookRegisterDto)){
            throw new BookNotFoundException("등록 할 책 정보 못 찾음");
        }
        if(!publisherRepository.existsById(bookRegisterDto.getPublisherId())){
            throw new PublisherNotFoundException("출판사 못 찾음");
        }

        Publisher publisher = publisherRepository.findById(bookRegisterDto.getPublisherId()).get();
        if(publisher.getPublisherName().isEmpty() || publisher.getPublisherId() == null){
            throw new PublisherNotFoundException("출판사 못 찾음");
        }

        Book book = new Book(
                publisher,
                bookRegisterDto.getBookTitle(),
                bookRegisterDto.getBookIndex(),
                bookRegisterDto.getBookDescription(),
                bookRegisterDto.getBookPubDate(),
                bookRegisterDto.getBookPriceStandard(),
                bookRegisterDto.getBookIsbn13()
        );
        bookRepository.save(book);
    }



    // 도서 삭제 기능 (관리자)
    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("존재하지 않는 도서 ID입니다.");
        }
        bookRepository.deleteById(bookId);
    }

    // 도서 수정 기능 (관리자)
    public void updateBook(Long bookId, BookRegisterDto bookUpdateRequest) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("존재하지 않는 도서 ID입니다."));
        book.setBookTitle(bookUpdateRequest.getBookTitle());
        book.setBookIndex(bookUpdateRequest.getBookIndex());
        book.setBookDescription(bookUpdateRequest.getBookDescription());
        book.setBookPubDate(bookUpdateRequest.getBookPubDate());
        book.setBookPriceStandard(bookUpdateRequest.getBookPriceStandard());
        book.setBookIsbn13(bookUpdateRequest.getBookIsbn13());
        bookRepository.save(book);
    }
}
