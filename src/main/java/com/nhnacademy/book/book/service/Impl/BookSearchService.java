package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.response.BookInfoResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.dto.response.BookSearchResponseDto;
import com.nhnacademy.book.book.elastic.document.*;
import com.nhnacademy.book.book.elastic.repository.*;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.SellingBookNotFoundException;
import com.nhnacademy.book.book.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookSearchService {

    private final BookRepository bookRepository;
    private final SellingBookRepository sellingBookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookImageRepository bookImageRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final SellingBookBookSearchRepository sellingBookBookRepository;
    private final BookSearchRepository bookSearchRepository;
    private final BookAuthorSearchRepository bookAuthorSearchRepository;
    private final BookSearchCategoryRepository bookCategorySearchRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorSearchRepository authorSearchRepository;
    private final CategorySearchRepository categorySearchRepository;
    private final BookInfoRepository bookInfoRepository;

    public List<BookDocument> getAllBooks(){

        return (List<BookDocument>) bookSearchRepository.findAll();
    }



    public Page<BookSearchResponseDto> searchBooksByKeyword(String keyword, Pageable pageable) {
        List<BookDocument> titleList = bookSearchRepository.findByBookTitleContaining(keyword);
        List<AuthorDocument> authorList = authorSearchRepository.findByAuthorNameContaining(keyword);
        List<CategoryDocument> categoryList = categorySearchRepository.findByCategoryNameContaining(keyword);

        // 중복 제거용 책 ID 저장
        Set<Long> bookIds = new HashSet<>();

        // 책 제목에서 책 ID 추출
        titleList.forEach(book -> bookIds.add(book.getBookId()));

        // 작가에서 책 ID 추출
        authorList.forEach(authorDoc -> {
            List<BookAuthorDocument> bookIdsFromAuthor = bookAuthorSearchRepository.findBookIdsByAuthorId(authorDoc.getAuthorId());
            bookIdsFromAuthor.forEach(bookAuthorDoc -> bookIds.add(bookAuthorDoc.getBookId()));
        });

        // 카테고리에서 책 ID 추출
        categoryList.forEach(categoryDoc -> {
            List<BookCategoryDocument> bookIdsFromCategory = bookCategorySearchRepository.findBookIdsByCategoryId(categoryDoc.getCategoryId());

            // bookIdsFromCategory에서 각 책 ID를 bookIds에 추가
            bookIdsFromCategory.forEach(bookCategoryDoc -> bookIds.add(bookCategoryDoc.getBookId()));
        });

        // 중복 제거 후 책 ID 리스트로 변환
        List<Long> pagedBookIds = new ArrayList<>(bookIds);

        // 페이징 처리: 원하는 페이지에 맞는 책 ID만 가져오기
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), pagedBookIds.size());

        // 판매책 정보 가져오기
        List<SellingBook> sellingBooks = sellingBookRepository.findByBook_BookIdIn(pagedBookIds.subList(start, end));

        // 카테고리, 작가, 이미지 정보 가져오기
        List<BookCategory> bookCategories = bookCategoryRepository.findByBook_BookIdIn(pagedBookIds.subList(start, end));
        List<BookAuthor> bookAuthors = bookAuthorRepository.findByBook_BookIdIn(pagedBookIds.subList(start, end));
        List<BookImage> bookImages = bookImageRepository.findByBook_BookIdIn(pagedBookIds.subList(start, end));

        List<BookSearchResponseDto> responseDtos = pagedBookIds.subList(start, end).stream().map(bookId -> {
            // 책 정보를 찾기
            Book book = bookRepository.findById(bookId).orElse(null); // 책 ID에 해당하는 책 찾기 (필요시 repository 수정)

            // 해당 책 ID에 맞는 sellingBook 찾기
            List<SellingBook> sellingBooksList = sellingBookRepository.findByBook_BookId(bookId);

            SellingBook sellingBook = (sellingBooksList.isEmpty()) ? null : sellingBooksList.getFirst();

            // 카테고리, 작가, 이미지 정보 가져오기
            List<String> categories = bookCategories.stream()
                    .filter(bookCategory -> bookCategory.getBook().getBookId().equals(bookId))
                    .map(bookCategory -> bookCategory.getCategory().getCategoryName())
                    .collect(Collectors.toList());

            List<String> authors = bookAuthors.stream()
                    .filter(bookAuthor -> bookAuthor.getBook().getBookId().equals(bookId))
                    .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                    .collect(Collectors.toList());

            List<String> bookImageList = bookImages.stream()
                    .filter(bookImage -> bookImage.getBook().getBookId().equals(bookId))
                    .map(BookImage::getImageUrl)
                    .collect(Collectors.toList());

            // DTO 변환: null일 경우 기본값 처리
            return new BookSearchResponseDto(
                    book != null ? book.getBookId() : 0L,  // null일 경우 0 처리
                    book != null ? book.getBookTitle() : "",  // null일 경우 공백 처리
                    book != null ? book.getBookIsbn13() : "",  // null일 경우 공백 처리
                    book != null ? book.getBookPriceStandard() : BigDecimal.valueOf(0.0),  // null일 경우 0 처리
                    sellingBook != null ? sellingBook.getSellingBookId() : 0L,  // null일 경우 0 처리
                    sellingBook != null ? sellingBook.getSellingBookPrice() : BigDecimal.valueOf(0.0),  // null일 경우 0 처리
                    sellingBook != null ? sellingBook.getSellingBookStock() : 0,  // null일 경우 0 처리
                    sellingBook != null ? sellingBook.getSellingBookStatus() : SellingBook.SellingBookStatus.safeValueOf(""),  // null일 경우 기본값 처리
                    authors,
                    categories,
                    bookImageList,
                    book != null ? book.getBookIndex() : "",  // null일 경우 공백 처리
                    book != null ? book.getBookDescription() : "",  // null일 경우 공백 처리
                    book != null ? book.getBookPubDate() : null  // null일 경우 null 처리
            );
        }).collect(Collectors.toList());

        // 전체 아이템 수 계산 (중복된 책 제외)
        long totalItems = bookIds.size();

        // 페이지 반환
        return new PageImpl<>(responseDtos, pageable, totalItems);
    }

    public Page<BookInfoResponseDto> searchBooksByKeyword2(String keyword, Pageable pageable) {
        // Elasticsearch에서 각 필드에 대한 검색 결과 가져오기
//        List<BookInfoDocument> booksByTitle = bookInfoRepository.searchBooksByTitle(keyword);
//        List<BookInfoDocument> booksByAuthor = bookInfoRepository.searchBooksByAuthor(keyword);
//        List<BookInfoDocument> booksByCategory = bookInfoRepository.searchBooksByCategory(keyword);

        List<BookInfoDocument> books = bookInfoRepository.searchBooksByKeyword(keyword);
        // 결과를 합치기
//        Set<BookInfoDocument> combinedResults = new HashSet<>();
//        combinedResults.addAll(booksByTitle);
//        combinedResults.addAll(booksByAuthor);
//        combinedResults.addAll(booksByCategory);

        // 리스트로 변환
//        List<BookInfoDocument> allResults = new ArrayList<>(combinedResults);

        // 페이징 처리를 위해 결과의 시작 인덱스와 끝 인덱스를 계산
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());

        // 페이징된 결과 추출
        List<BookInfoDocument> pagedBooks = books.subList(start, end);

        // DTO로 변환
        List<BookInfoResponseDto> bookInfoResponseDtos = pagedBooks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 페이징 처리된 결과 반환
        return new PageImpl<>(bookInfoResponseDtos, pageable, books.size());
    }



    private BookInfoResponseDto convertToDto(BookInfoDocument bookInfoDocument) {
        return new BookInfoResponseDto(
                bookInfoDocument.getBookId(),
                bookInfoDocument.getBookTitle(),
                bookInfoDocument.getPublisherName(),
                bookInfoDocument.getCategoryName(),
                bookInfoDocument.getAuthorName(),
                bookInfoDocument.getSellingBookPrice(),
                bookInfoDocument.getBookPriceStandard(),
                bookInfoDocument.getImageUrl(),
                bookInfoDocument.getSellingBookId()
        );
    }





}
