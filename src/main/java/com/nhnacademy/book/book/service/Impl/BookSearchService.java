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



    private final BookInfoRepository bookInfoRepository;



    public Page<BookInfoResponseDto> searchBooksByKeyword2(String keyword, Pageable pageable) {

        // 키워드로 검색
        List<BookInfoDocument> books = bookInfoRepository.searchBooksByKeyword(keyword);

        if (books.isEmpty()) {
            throw new BookNotFoundException("No books found for keyword: " + keyword);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());

        // 페이징 범위 확인
        if (start >= books.size()) {
            throw new IllegalArgumentException("Requested page is out of bounds.");
        }

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
