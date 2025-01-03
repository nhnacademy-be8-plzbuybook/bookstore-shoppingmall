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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
