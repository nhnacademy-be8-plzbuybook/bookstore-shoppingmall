package com.nhnacademy.book.booktest.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.book.book.dto.response.BookTagResponseDto;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookTag;
import com.nhnacademy.book.book.entity.Tag;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.TagNotFoundException;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.BookTagRepository;
import com.nhnacademy.book.book.repository.TagRepository;
import com.nhnacademy.book.book.service.Impl.BookService;
import com.nhnacademy.book.book.service.Impl.BookTagService;
import com.nhnacademy.book.book.service.Impl.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

public class BookTagServiceTest {

    @Mock
    private BookTagRepository bookTagRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagService tagService;

    @InjectMocks
    private BookTagService bookTagService;

    private Book book;
    private Tag tag;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book(1L, "Test Book");
        tag = new Tag(1L, "Science");

        when(bookService.existsBook(1L)).thenReturn(true);
        when(bookRepository.findByBookId(1L)).thenReturn(book);

        when(tagService.existsTag(1L)).thenReturn(true);
        when(tagService.findTagById(1L)).thenReturn(tag);
    }

    @Test
    void testSave() {
        Long bookId = 1L;
        Long tagId = 1L;
        when(bookRepository.existsByBookId(bookId)).thenReturn(true);

        when(tagRepository.existsByTagId(tagId)).thenReturn(true);


        bookTagService.save(bookId, tagId);

        verify(bookTagRepository, times(1)).save(any(BookTag.class));
    }

    @Test
    void testSave_bookNotFound() {
        Long bookId = 1L;
        Long tagId = 1L;
        when(bookRepository.existsByBookId(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookTagService.save(bookId, tagId));
        verify(bookTagRepository, never()).save(any(BookTag.class));
    }
    @Test
    void testSave_tagNotFound() {
        Long bookId = 1L;
        Long tagId = 1L;
        when(bookRepository.existsByBookId(bookId)).thenReturn(true);
        when(tagRepository.existsByTagId(tagId)).thenReturn(false);



        assertThrows(TagNotFoundException.class, () -> bookTagService.save(bookId, tagId));
        verify(bookTagRepository, never()).save(any(BookTag.class));
    }

    @Test
    void testGetBookTagList() {
        List<BookTagResponseDto> bookTags = Arrays.asList(
                new BookTagResponseDto(1L, tag.getTagId(), tag.getTagName(), book.getBookId(), book.getBookTitle())
        );
        when(bookTagRepository.findBookTagResponseDtosByTagId(1L)).thenReturn(bookTags);

        List<BookTagResponseDto> result = bookTagService.getBookTagList(1L);

        assertEquals(1, result.size());
        assertEquals("Science", result.get(0).getTagName());
    }

    @Test
    void testDeleteBookTagList() {
        Long bookId = 1L;
        Long tagId = 1L;


        bookTagService.deleteBookTagList(bookId, tagId);


        verify(bookTagRepository, times(1)).deleteByBook_BookIdAndTag_TagId(bookId, tagId);
    }

    @Test
    void testGetBookTagListByBookId() {

        BookTag bookTag = new BookTag();
        bookTag.setBookTagId(1L);
        bookTag.setBook(book);
        bookTag.setTag(tag);
        List<BookTag> bookTags = Arrays.asList(
                bookTag
        );
        when(bookTagRepository.findBookTagByBook_BookId(1L)).thenReturn(bookTags);

        List<BookTagResponseDto> result = bookTagService.getBookTagListByBookId(1L);

        assertEquals(1, result.size());
        assertEquals("Science", result.get(0).getTagName());
        assertEquals("Test Book", result.get(0).getBookName());
    }

    @Test
    void testGetBookTagListByBookId_emptyList() {
        when(bookTagRepository.findBookTagByBook_BookId(1L)).thenReturn(Arrays.asList());

        List<BookTagResponseDto> result = bookTagService.getBookTagListByBookId(1L);

        assertTrue(result.isEmpty());
    }
}

