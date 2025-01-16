package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.*;
import com.nhnacademy.book.book.dto.response.*;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.BookRegisterDto;
import com.nhnacademy.book.book.elastic.document.BookDocument;
import com.nhnacademy.book.book.elastic.repository.BookInfoRepository;
import com.nhnacademy.book.book.elastic.repository.BookSearchRepository;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final BookAuthorRepository bookAuthorRepository;
    private final BookInfoRepository bookInfoRepository;

    @PersistenceContext
    private EntityManager entityManager;



    public boolean existsBook(Long bookId){
        if(bookRepository.existsById(bookId)){
            return true;
        } else {
            throw new BookNotFoundException("Book not found");
        }
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


    // 도서 등록 기능 (관리자)
    @Transactional
    public void registerBook(com.nhnacademy.book.book.dto.request.BookRegisterDto bookRegisterDto) {
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

    /**
     * 관리자페이지에서 페이징 처리만 함.
     * @param pageable
     * @return
     */
    public Page<BookRegisterDto> getBooks(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);

        return bookPage.map(book -> {
            List<String> bookImage = bookImageRepository.findByBook_BookId(book.getBookId())
                    .stream()
                    .map(BookImage::getImageUrl)
                    .collect(Collectors.toList());

            // 카테고리 정보 매핑
            List<Category> categories = categoryRepository.findCategoriesByBookId(book.getBookId());

            // 작가 정보 매핑
            List<Author> authors = bookAuthorRepository.findAuthorsByBookId(book.getBookId());

            // 출판사 정보 가져오기
            String publisher = book.getPublisher().getPublisherName();

            return new BookRegisterDto(
                    book.getBookId(),
                    book.getBookTitle(),    // 제목
                    book.getBookPubDate(),         // 출판일
                    publisher,                     // 출판사
                    book.getBookIsbn13(),          // ISBN
                    book.getBookPriceStandard(),
                    bookImage, // 이미지 URL
                    authors.stream()
                            .map(author -> new AuthorResponseDto(
                                    author.getAuthorId(),
                                    author.getAuthorName()
                            ))
                            .toList(),
                    categories.stream()
                            .map(category -> new CategorySimpleResponseDto(
                                    category.getCategoryId(),
                                    category.getCategoryName()
                            ))
                            .toList()
            );
        });
    }






        // 도서 삭제 기능 (관리자)
    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("존재하지 않는 도서 ID입니다.");
        }
        bookRepository.deleteById(bookId);
        bookInfoRepository.deleteByBookId(bookId);

    }

    // 도서 수정 기능 (관리자)
    public void updateBook(Long bookId, com.nhnacademy.book.book.dto.request.BookRegisterDto bookUpdateRequest) {
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

    public Page<BookResponseDto> findBooksNotInSellingBooks(Pageable pageable) {
        // 캐시 초기화
        entityManager.clear();

        // 레포지토리에서 데이터 조회
        Page<Book> booksPage = bookRepository.findBooksNotInSellingBooks(pageable);

        // 엔티티를 DTO로 변환하여 반환
        return booksPage.map(book -> new BookResponseDto(
                book.getBookId(),
                book.getBookTitle(),
                book.getBookPriceStandard(),
                book.getBookIsbn13(),
                book.getBookPubDate(),
                book.getPublisher().getPublisherName()
        ));
    }

    public void setEntityManager(EntityManager entityManager) {
    }
}
