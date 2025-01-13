package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.response.BookTagResponseDto;
import com.nhnacademy.book.book.dto.response.TagResponseDto;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookTag;
import com.nhnacademy.book.book.entity.Tag;
import com.nhnacademy.book.book.exception.TagNotFoundException;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.BookTagRepository;
import com.nhnacademy.book.book.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookTagService {

    @Autowired
    private final BookTagRepository bookTagRepository;

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final BookService bookService;

    @Autowired
    private final TagRepository tagRepository;

    @Autowired
    private final TagService tagService;

    public void save(Long bookId, Long tagId) {
        BookTag bookTag = new BookTag();
        Book book;
        if(bookService.existsBook(bookId)){
            book = bookRepository.findByBookId(bookId);
            bookTag.setBook(book);

        }
        Tag tag;
        if(tagService.existsTag(tagId)){
            tag = tagService.findTagById(tagId);
            bookTag.setTag(tag);
        }

        bookTagRepository.save(bookTag);

    }

    public List<BookTagResponseDto> getBookTagList(Long tagId) {

        return bookTagRepository.findBookTagResponseDtosByTagId(tagId);

    }

    public void deleteBookTagList(Long bookId,Long tagId) {
        bookTagRepository.deleteByBook_BookIdAndTag_TagId(bookId,tagId);
    }

    public List<BookTagResponseDto> getBookTagListByBookId(Long bookId) {
        List<BookTag> bookTags = bookTagRepository.findBookTagByBook_BookId(bookId);
        return bookTags.stream()
                .map(bookTag -> new BookTagResponseDto(bookTag.getBookTagId() ,bookTag.getTag().getTagId(), bookTag.getTag().getTagName(), bookTag.getBook().getBookId(),bookTag.getBook().getBookTitle()))
                .collect(Collectors.toList());

    }


}
