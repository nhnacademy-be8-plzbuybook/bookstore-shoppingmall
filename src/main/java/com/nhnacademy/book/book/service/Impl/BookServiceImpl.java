package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.*;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.repository.*;
import com.nhnacademy.book.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final BookImageRepository bookImageRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository,
                           CategoryRepository categoryRepository,
                           PublisherRepository publisherRepository,
                           BookImageRepository bookImageRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.bookImageRepository = bookImageRepository;
    }

    // 도서 검색 기능
    @Override
    public List<BookResponseDto> searchBooks(BookSearchRequestDto searchRequest) {
        return bookRepository.findByBookTitleContaining(searchRequest.getKeyword())
                .stream()
                .map(book -> new BookResponseDto(
                        book.getBookId(),
                        book.getBookTitle(),
                        book.getBookPriceStandard(),
                        book.getBookIsbn13()))
                .collect(Collectors.toList());
    }

    // 도서 상세 조회 기능
    @Override
    public BookDetailResponseDto getBookDetail(Long sellingId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도서 ID입니다."));
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

    // 도서 좋아요 기능
    @Override
    public void likeBook(Long bookId, BookLikeRequestDto likeRequest) {
        // 좋아요 처리 로직 (DB 업데이트 등)
        System.out.println("Book ID " + bookId + " 좋아요 처리 완료.");
    }

    // 도서 이미지 업로드 기능
    @Override
    public void uploadBookImage(Long bookId, MultipartFile file) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도서 ID입니다."));
        BookImage bookImage = new BookImage();
        bookImage.setBook(book);
        bookImage.setImageId(System.currentTimeMillis()); // 파일 저장 로직 필요
        bookImageRepository.save(bookImage);
        System.out.println("Book ID " + bookId + " 이미지 업로드 완료: " + file.getOriginalFilename());
    }

    // 도서 태그 지정 기능
    @Override
    public void updateBookTags(Long bookId, BookTagRequestDto tagRequest) {
        // 태그 업데이트 로직
        System.out.println("Book ID " + bookId + " 태그 업데이트 완료: " + tagRequest.getTags());
    }

    // 도서 등록 기능 (관리자)
    @Override
    public void registerBook(BookRegisterDto bookRegisterDto) {
        Publisher publisher = publisherRepository.findById(bookRegisterDto.getPublisherId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출판사 ID입니다."));
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
        System.out.println("도서 등록 완료: " + book.getBookTitle());
    }

    // 도서 카테고리 등록 기능 (관리자)
    @Override
    public void registerCategory(BookCategoryRegisterDto categoryRequest) {
        Category parentCategory = null;
        if (categoryRequest.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(categoryRequest.getParentCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부모 카테고리 ID입니다."));
        }
        Category category = new Category(categoryRequest.getCategoryName(), 1, parentCategory);
        categoryRepository.save(category);
        System.out.println("카테고리 등록 완료: " + category.getCategoryName());
    }

    // 도서 삭제 기능 (관리자)
    @Override
    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new IllegalArgumentException("존재하지 않는 도서 ID입니다.");
        }
        bookRepository.deleteById(bookId);
        System.out.println("도서 삭제 완료: Book ID " + bookId);
    }

    // 도서 수정 기능 (관리자)
    @Override
    public void updateBook(Long bookId, BookRegisterDto bookUpdateRequest) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도서 ID입니다."));
        book.setBookTitle(bookUpdateRequest.getBookTitle());
        book.setBookIndex(bookUpdateRequest.getBookIndex());
        book.setBookDescription(bookUpdateRequest.getBookDescription());
        book.setBookPubDate(bookUpdateRequest.getBookPubDate());
        book.setBookPriceStandard(bookUpdateRequest.getBookPriceStandard());
        book.setBookIsbn13(bookUpdateRequest.getBookIsbn13());
        bookRepository.save(book);
        System.out.println("도서 수정 완료: " + book.getBookTitle());
    }
}
