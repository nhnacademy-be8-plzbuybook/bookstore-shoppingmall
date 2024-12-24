package com.nhnacademy.book.book.elastic.mapper;


import com.nhnacademy.book.book.elastic.document.BookDocument;
import com.nhnacademy.book.book.entity.Book;
import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {

    public static BookDocument toBookDocument(Book book) {
        // Publisher name 가져오기
        Long publisherId = book.getPublisher().getPublisherId();

        // BookAuthor를 통해 저자 정보 가져오기
        List<String> authors = book.getBookAuthors().stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                .collect(Collectors.toList());

        // BookDocument로 변환
        return new BookDocument(
                book.getBookId(),
                book.getBookTitle(),
                book.getBookDescription(),
                book.getBookPubDate(),
                book.getBookPriceStandard(),
                book.getBookIsbn13(),
                publisherId,
                authors
        );
    }
}
