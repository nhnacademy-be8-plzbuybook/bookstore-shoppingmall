package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.response.BookTagResponseDto;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookTag;
import com.nhnacademy.book.book.entity.Tag;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.TagNotFoundException;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.BookTagRepository;
import com.nhnacademy.book.book.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookTagService {
    private final BookTagRepository bookTagRepository;
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final TagService tagService;

    public void save(Long bookId, Long tagId) {
        BookTag bookTag = new BookTag();
        Book book;
        if (bookRepository.existsByBookId(bookId)) {
            book = bookRepository.findByBookId(bookId);
            bookTag.setBook(book);
        } else {
            throw new BookNotFoundException("book not found");
        }
        Tag tag;
        if (tagRepository.existsByTagId(tagId)) {
            tag = tagService.findTagById(tagId);
            bookTag.setTag(tag);
        } else {
            throw new TagNotFoundException("tag not found");
        }

        bookTagRepository.save(bookTag);

    }

    public List<BookTagResponseDto> getBookTagList(Long tagId) {

        return bookTagRepository.findBookTagResponseDtosByTagId(tagId);

    }

    public void deleteBookTagList(Long bookId, Long tagId) {
        bookTagRepository.deleteByBook_BookIdAndTag_TagId(bookId, tagId);
    }

    public List<BookTagResponseDto> getBookTagListByBookId(Long bookId) {
        List<BookTag> bookTags = bookTagRepository.findBookTagByBook_BookId(bookId);
        return bookTags.stream()
                .map(bookTag -> new BookTagResponseDto(bookTag.getBookTagId(), bookTag.getTag().getTagId(), bookTag.getTag().getTagName(), bookTag.getBook().getBookId(), bookTag.getBook().getBookTitle()))
                .toList();

    }


}
