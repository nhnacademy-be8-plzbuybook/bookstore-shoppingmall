package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.response.BookInfoResponseDto;
import com.nhnacademy.book.book.elastic.document.BookInfoDocument;
import com.nhnacademy.book.book.elastic.repository.BookInfoRepository;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.exception.CategoryNotFoundException;
import com.nhnacademy.book.book.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookSearchService {

    private final BookInfoRepository bookInfoRepository;

    private final CategoryRepository categoryRepository;

    public Page<BookInfoResponseDto> searchBooksByKeyword2(String keyword, Pageable pageable) {

        List<BookInfoDocument> books = bookInfoRepository.searchBooksByKeyword(keyword);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());

        List<BookInfoDocument> pagedBooks = books.subList(start, end);

        List<BookInfoResponseDto> bookInfoResponseDtos = pagedBooks.stream()
                .map(this::convertToDto)
                .toList();

        return new PageImpl<>(bookInfoResponseDtos, pageable, books.size());
    }

    public Page<BookInfoResponseDto> findByExactCategoryName(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findByCategoryId(categoryId).orElseThrow(() -> new CategoryNotFoundException("category Not Found"));
        List<BookInfoDocument> books = bookInfoRepository.findByExactCategoryName(category.getCategoryName());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());

// start가 books.size()보다 크면 빈 리스트 반환
        List<BookInfoDocument> pagedBooks = (start < books.size())
                ? books.subList(start, end)
                : Collections.emptyList();

        List<BookInfoResponseDto> bookInfoResponseDtos = pagedBooks.stream()
                .map(this::convertToDto)
                .toList();

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
