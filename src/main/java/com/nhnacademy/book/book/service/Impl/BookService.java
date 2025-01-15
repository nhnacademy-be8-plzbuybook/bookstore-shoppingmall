package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.*;
import com.nhnacademy.book.book.dto.response.AdminBookAndSellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.AdminBookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.elastic.document.BookDocument;
import com.nhnacademy.book.book.elastic.repository.BookSearchRepository;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.PublisherNotFoundException;
import com.nhnacademy.book.book.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BookService {

    //TODO 수정

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final BookSearchRepository bookSearchRepository;
    private final BookImageRepository bookImageRepository;
    private final SellingBookRepository sellingBookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final BookCategoryService bookCategoryService;
    private final BookAuthorService bookAuthorService;
    private final AuthorService authorService;


    public boolean existsBook(Long bookId){
        if(bookRepository.existsById(bookId)){
            return true;
        } else {
            throw new BookNotFoundException("Book not found");
        }
    }


    public List<BookDetailResponseDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        return books.stream()
                .map(book -> {
                    SellingBook sellingBook = sellingBookRepository.findByBook(book);
                    BookImage bookImage = bookImageRepository.findByBook(book).orElse(null);
                    return new BookDetailResponseDto(
                            book.getBookId(),
                            sellingBook != null ? sellingBook.getSellingBookId() : null, // SellingBook ID 추가
                            book.getBookTitle(),
                            book.getBookIndex(),
                            book.getBookDescription(),
                            book.getBookPubDate(),
                            book.getBookPriceStandard(),
                            book.getBookIsbn13(),
                            book.getPublisher().getPublisherId(),
                            bookImage != null ? bookImage.getImageUrl() : null // 이미지 URL 추가
                    );
                })
                .collect(Collectors.toList());

    }

    // 도서 상세 조회 기능
    public BookDetailResponseDto getBookDetail(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("존재하지 않는 도서 ID입니다."));

        BookImage bookImage = bookImageRepository.findByBook(book).orElse(null);

        return new BookDetailResponseDto(
                book.getBookId(),
                book.getBookTitle(),
                book.getBookIndex(),
                book.getBookDescription(),
                book.getBookPubDate(),
                book.getBookPriceStandard(),
                book.getBookIsbn13(),
                book.getPublisher().getPublisherId(),
                bookImage != null ? bookImage.getImageUrl() : null // 이미지 URL 추가
        );
    }

    public BookDetailResponseDto getBookDetailFromElastic(Long bookId) {
        // Elasticsearch에서 BookDocument 조회
        log.debug("Searching for book with ID: {}", bookId);

        BookDocument bookDocument = bookSearchRepository.findByBookId(bookId);

        if (bookDocument == null) {
            log.error("Book not found with ID: {}", bookId);

            throw new BookNotFoundException("Book with ID " + bookId + " not found.");
        }


        log.debug("Found book: {}", bookDocument);

        // BookDetailResponseDto로 변환하여 반환
        return new BookDetailResponseDto(
                bookDocument.getBookId(),
                bookDocument.getBookTitle(),
                bookDocument.getBookIndex(),
                bookDocument.getBookDescription(),
                bookDocument.getBookPubDate(),
                bookDocument.getBookPriceStandard(),
                bookDocument.getBookIsbn13(),
                bookDocument.getPublisherId(),
                bookDocument.getImageUrl());
    }




    // 도서 등록 기능 (관리자)
    @Transactional
    public void registerBook(BookRegisterDto bookRegisterDto) {
        if(Objects.isNull(bookRegisterDto)){
            throw new BookNotFoundException("등록 할 책 정보 못 찾음");
        }
        // 출판사 조회 및 필요시 등록
        Publisher publisher = publisherRepository.findByPublisherName(bookRegisterDto.getPublisher())
                .orElseGet(() -> {
                    Publisher newPublisher = new Publisher(bookRegisterDto.getPublisher());
                    return publisherRepository.save(newPublisher); // 새 출판사 저장
                });



        Book book = new Book(
                publisher,
                bookRegisterDto.getBookTitle(),
                bookRegisterDto.getBookIndex(),
                bookRegisterDto.getBookDescription(),
                bookRegisterDto.getBookPubDate(),
                bookRegisterDto.getBookPriceStandard(),
                bookRegisterDto.getBookIsbn13()
        );
        Book book2 = bookRepository.save(book);


        // 3. 이미지 등록
        if (bookRegisterDto.getImageUrl() != null) {
            BookImage bookImage = new BookImage(book, bookRegisterDto.getImageUrl());
            bookImageRepository.save(bookImage); // 명시적으로 저장
        }
        Category ca = categoryRepository.findByCategoryId(bookRegisterDto.getCategories().getFirst().getCategoryId()).get();
        // 4. 카테고리 등록
        List<Category> categories = bookRegisterDto.getCategories().stream()
                .map(category -> categoryRepository.findByCategoryId(category.getCategoryId()).get()
                ).toList();

//        categories.forEach(book::addCategory);

        for(Category category : categories) {
            BookCategoryRequestDto requestDto = new BookCategoryRequestDto();
            requestDto.setCategoryId(category.getCategoryId());
            requestDto.setBookId(book.getBookId());
            bookCategoryService.createBookCategory(requestDto);
        }



        // 5. 작가 등록
        List<Author> authors = bookRegisterDto.getAuthors().stream()
                .map(authorName -> authorRepository.findByAuthorName(authorName)
                        .orElseGet(() -> authorRepository.save(new Author(authorName))))
                .toList();
        for(Author author : authors) {
            BookAuthorRequestDto requestDto = new BookAuthorRequestDto();
            requestDto.setAuthorId(author.getAuthorId());
            requestDto.setBookId(book.getBookId());
            bookAuthorService.createBookAuthor(requestDto);
        }


    }


    //TODO
    @Transactional
    public void registerBookAndSellingBooks(SellingBookRegisterDto sellingBookRegisterDto) {
        // 1. 책 ID로 책 정보 조회
        Book book = bookRepository.findById(sellingBookRegisterDto.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책 ID입니다."));

        // 2. 판매책 정보 생성
        SellingBook sellingBook = new SellingBook();
        sellingBook.setBook(book); // 책과 매핑
        sellingBook.setSellingBookPrice(sellingBookRegisterDto.getSellingBookPrice()); // 판매가
        sellingBook.setSellingBookPackageable(sellingBookRegisterDto.getSellingBookPackageable()); // 선물포장 가능 여부
        sellingBook.setSellingBookStock(sellingBookRegisterDto.getSellingBookStock()); // 재고
        sellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.safeValueOf(String.valueOf(sellingBookRegisterDto.getSellingBookStatus()))); // 도서 상태
        sellingBook.setSellingBookViewCount(sellingBookRegisterDto.getSellingBookViewCount() != null
                ? sellingBookRegisterDto.getSellingBookViewCount() : 0L); // 조회수 (기본값 0)
        sellingBook.setUsed(sellingBookRegisterDto.getUsed() != null ? sellingBookRegisterDto.getUsed() : false); // 중고 여부 (기본값 false)

        // 3. 판매책 저장
        sellingBookRepository.save(sellingBook);

        // 로그 출력 (디버깅 용도)
        log.info("판매책 등록 완료: {}", sellingBook);

    }





        // 도서 삭제 기능 (관리자)
    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("존재하지 않는 도서 ID입니다.");
        }
        bookRepository.deleteById(bookId);
        bookSearchRepository.deleteById(bookId);
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
