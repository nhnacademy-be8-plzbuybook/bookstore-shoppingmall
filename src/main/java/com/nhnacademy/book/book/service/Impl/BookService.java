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

    // 도서 수정 값관련 서비스
    public BookRegisterRequestDto getBookUpdate(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("존재하지 않는 도서 ID입니다."));
        String imageUrl = book.getBookImages().isEmpty() ? null : book.getBookImages().get(0).getImageUrl();

        // Debugging
        log.debug("도서 정보: {}", book);

        // 작가 정보 추출
        List<String> authors = book.getBookAuthors().stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                .collect(Collectors.toList());

        List<CategoryResponseDto> categoryDtos = book.getBookCategories().stream()
                .map(bookCategory -> new CategoryResponseDto(
                        bookCategory.getCategory().getCategoryId(),
                        bookCategory.getCategory().getCategoryName(),
                        bookCategory.getCategory().getCategoryDepth(),
                        bookCategory.getCategory().getParentCategory() != null
                                ? bookCategory.getCategory().getParentCategory().getCategoryId()
                                : null,
                        null // 자식 카테고리 필요 시 추가 처리
                ))
                .collect(Collectors.toList());

        return new BookRegisterRequestDto(
                book.getBookId(),
                book.getBookTitle(),
                book.getBookIndex(),
                book.getBookDescription(),
                book.getBookPubDate(),
                book.getBookPriceStandard(),
                book.getBookIsbn13(),
                book.getPublisher().getPublisherName(),
                imageUrl,
                categoryDtos,
                authors
        );

    }


    // 도서 등록 기능 (관리자)
    @Transactional
    public void registerBook(BookRegisterRequestDto bookRegisterDto) {
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

//    // 도서 수정 기능 (관리자)
    public void updateBook(BookRegisterRequestDto bookUpdateRequest) {

        //1. 도서 정보 조회
        Book book = bookRepository.findById(bookUpdateRequest.getBookId())
                .orElseThrow(() -> new BookNotFoundException("존재하지 않는 도서 ID입니다."));
        // 2. 특정 필드만 수정
        if (bookUpdateRequest.getBookTitle() != null) {
            book.setBookTitle(bookUpdateRequest.getBookTitle());
        }
        if (bookUpdateRequest.getBookIndex() != null) {
            book.setBookIndex(bookUpdateRequest.getBookIndex());
        }
        if (bookUpdateRequest.getBookDescription() != null) {
            book.setBookDescription(bookUpdateRequest.getBookDescription());
        }
        if (bookUpdateRequest.getBookPubDate() != null) {
            book.setBookPubDate(bookUpdateRequest.getBookPubDate());
        }
        if (bookUpdateRequest.getBookPriceStandard() != null) {
            book.setBookPriceStandard(bookUpdateRequest.getBookPriceStandard());
        }
        // ISBN은 수정하지 않음
        bookRepository.save(book);


        // 3. 출판사 업데이트
        if (bookUpdateRequest.getPublisher() != null) {
            Publisher publisher = publisherRepository.findByPublisherName(bookUpdateRequest.getPublisher())
                    .orElseGet(() -> {
                        Publisher newPublisher = new Publisher(bookUpdateRequest.getPublisher());
                        return publisherRepository.save(newPublisher);
                    });
            book.setPublisher(publisher);
        }


        // 4. 이미지 등록
        if (bookUpdateRequest.getImageUrl() != null) {
            book.getBookImages().clear();
            bookImageRepository.deleteAllByBook(book);
            BookImage bookImage = new BookImage(book, bookUpdateRequest.getImageUrl());
            bookImageRepository.save(bookImage); // 저장
        }


        // 5. 기존 카테고리 제거 및 새 카테고리 등록
        if (bookUpdateRequest.getCategories() != null && !bookUpdateRequest.getCategories().isEmpty()) {
            book.getBookCategories().clear();
            List<Category> categories = bookUpdateRequest.getCategories().stream()
                    .map(category -> categoryRepository.findByCategoryId(category.getCategoryId())
                            .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리 ID입니다: " + category.getCategoryId())))
                    .toList();

            for (Category category : categories) {
                BookCategoryRequestDto requestDto = new BookCategoryRequestDto();
                requestDto.setCategoryId(category.getCategoryId());
                requestDto.setBookId(book.getBookId());
                bookCategoryService.createBookCategory(requestDto);
            }
        }

        // 6. 작가 등록
        if (bookUpdateRequest.getAuthors() != null && !bookUpdateRequest.getAuthors().isEmpty()) {
            book.getBookAuthors().clear();
            List<Author> authors = bookUpdateRequest.getAuthors().stream()
                    .map(authorName -> authorRepository.findByAuthorName(authorName)
                            .orElseGet(() -> authorRepository.save(new Author(authorName))))
                    .toList();

            for (Author author : authors) {
                BookAuthorRequestDto requestDto = new BookAuthorRequestDto();
                requestDto.setAuthorId(author.getAuthorId());
                requestDto.setBookId(book.getBookId());
                bookAuthorService.createBookAuthor(requestDto);
            }
        }
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
        this.entityManager = entityManager;
    }
}
