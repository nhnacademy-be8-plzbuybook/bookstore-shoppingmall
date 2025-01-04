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


    private final BookSearchRepository bookSearchRepository;

    private final BookInfoRepository bookInfoRepository;

    public List<BookDocument> getAllBooks(){

        return (List<BookDocument>) bookSearchRepository.findAll();
    }



    public Page<BookInfoResponseDto> searchBooksByKeyword2(String keyword, Pageable pageable) {

        List<BookInfoDocument> books = bookInfoRepository.searchBooksByKeyword(keyword);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());

        List<BookInfoDocument> pagedBooks = books.subList(start, end);

        List<BookInfoResponseDto> bookInfoResponseDtos = pagedBooks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

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
