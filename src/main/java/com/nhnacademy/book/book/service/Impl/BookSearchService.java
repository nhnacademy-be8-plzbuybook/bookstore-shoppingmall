package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.response.BookSearchResponseDto;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookSearchService {

    private final BookRepository bookRepository;
    private final SellingBookRepository sellingBookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookImageRepository bookImageRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @Autowired
    public BookSearchService(BookRepository bookRepository, SellingBookRepository sellingBookRepository, BookAuthorRepository bookAuthorRepository, BookCategoryRepository bookCategoryRepository, BookImageRepository bookImageRepository) {
        this.bookRepository = bookRepository;
        this.sellingBookRepository = sellingBookRepository;
        this.bookAuthorRepository = bookAuthorRepository;
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookImageRepository = bookImageRepository;
    }


    public List<BookSearchResponseDto> searchBooks(String searchKeyword) {
        // 책 검색
        List<Book> books = bookRepository.findBooksBySearchKeyword(searchKeyword);

        // 결과를 DTO로 변환하여 판매책 정보도 함께 가져옴
        return books.stream().map(book -> {
            // 판매책 정보 가져오기
            SellingBook sellingBook = sellingBookRepository.findByBook_BookId(book.getBookId());

            // 책에 연결된 작가 이름 가져오기
            List<String> authorNames = bookAuthorRepository.findAuthorsByBookId(book.getBookId())
                    .stream()
                    .map(Author::getAuthorName) // Author 엔티티에서 이름만 추출
                    .collect(Collectors.toList());

            // 책에 연결된 카테고리 이름 가져오기
            List<String> categoryNames = bookCategoryRepository.findCategoriesByBookId(book.getBookId())
                    .stream()
                    .map(Category::getCategoryName) // Category 엔티티에서 이름만 추출
                    .collect(Collectors.toList());

            List<String> image = bookImageRepository.findByBook(book).isPresent()
                    ? bookImageRepository.findByBook(book)
                    .stream()
                    .map(BookImage::getImageUrl)
                    .collect(Collectors.toList())
                    : new ArrayList<>();  // 이미지가 없으면 빈 리스트 반환

            return new BookSearchResponseDto(
                    book.getBookId(),
                    book.getBookTitle(),
                    book.getBookIsbn13(),
                    book.getBookPriceStandard(),
                    sellingBook != null ? sellingBook.getSellingBookId() : null,
                    sellingBook != null ? sellingBook.getSellingBookPrice() : null,
                    sellingBook != null ? sellingBook.getSellingBookStock() : null,
                    sellingBook != null ? sellingBook.getSellingBookStatus() : null,
                    authorNames,  // 작가 이름 리스트
                    categoryNames, // 카테고리 이름 리스트
                    image,
                    book.getBookIndex(),
                    book.getBookDescription(),
                    book.getBookPubDate()
            );
        }).collect(Collectors.toList());
    }
}
