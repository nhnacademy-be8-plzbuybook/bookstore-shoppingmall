package com.nhnacademy.book.book.service.Impl;


import com.nhnacademy.book.book.dto.request.TagRegisterDto;
import com.nhnacademy.book.book.dto.response.TagResponseDto;
import com.nhnacademy.book.book.entity.Tag;
import com.nhnacademy.book.book.exception.TagNameAlreadyException;
import com.nhnacademy.book.book.exception.TagNotFoundException;
import com.nhnacademy.book.book.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setTagId(1L);
        tag.setTagName("Fiction");
    }

    @Test
    void testSaveTag_success() {
        TagRegisterDto tagRegisterDto = new TagRegisterDto();
        tagRegisterDto.setTagName("Fiction");

        when(tagRepository.existsByTagName("Fiction")).thenReturn(false);
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        TagResponseDto response = tagService.save(tagRegisterDto);

        assertEquals("Fiction", response.getTagName());
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void testSaveTag_TagNameAlreadyExists() {
        TagRegisterDto tagRegisterDto = new TagRegisterDto();
        tagRegisterDto.setTagName("Fiction");

        when(tagRepository.existsByTagName("Fiction")).thenReturn(true);

        TagNameAlreadyException exception = assertThrows(TagNameAlreadyException.class, () -> tagService.save(tagRegisterDto));
        assertEquals("이미 존재하는 태그입니다.", exception.getMessage());
    }

    @Test
    void testFindTagById_success() {
        when(tagRepository.existsByTagId(1L)).thenReturn(true);
        when(tagRepository.findByTagId(1L)).thenReturn(tag);

        Tag foundTag = tagService.findTagById(1L);

        assertNotNull(foundTag);
        assertEquals("Fiction", foundTag.getTagName());
    }

    @Test
    void testFindTagById_TagNotFound() {
        when(tagRepository.existsByTagId(2L)).thenReturn(false);

        TagNotFoundException exception = assertThrows(TagNotFoundException.class, () -> tagService.findTagById(2L));
        assertEquals("tag not found", exception.getMessage());
    }

    @Test
    void testSearchTagsByKeyword() {
        Pageable pageable = Pageable.ofSize(5);
        List<Tag> tags = Arrays.asList(tag);
        Page<Tag> page = new PageImpl<>(tags);

        when(tagRepository.findByTagNameContaining("Fict", pageable)).thenReturn(page);

        Page<TagResponseDto> result = tagService.searchTagsByKeyword("Fict", pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Fiction", result.getContent().get(0).getTagName());
    }

    @Test
    void testDeleteTagById_success() {
        when(tagRepository.existsByTagId(1L)).thenReturn(true);

        tagService.deleteTagById(1L);

        verify(tagRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTagById_TagNotFound() {
        when(tagRepository.existsByTagId(2L)).thenReturn(false);

        TagNotFoundException exception = assertThrows(TagNotFoundException.class, () -> tagService.deleteTagById(2L));
        assertEquals("tag not found", exception.getMessage());
    }

    @Test
    void testFindTagNameByTagId_success() {
        when(tagRepository.existsByTagId(1L)).thenReturn(true);
        when(tagRepository.findTagByTagId(1L)).thenReturn(tag);

        String tagName = tagService.findTagNameByTagId(1L);

        assertEquals("Fiction", tagName);
    }

    @Test
    void testFindTagNameByTagId_TagNotFound() {
        when(tagRepository.existsByTagId(2L)).thenReturn(false);

        TagNotFoundException exception = assertThrows(TagNotFoundException.class, () -> tagService.findTagNameByTagId(2L));
        assertEquals("tag not found", exception.getMessage());
    }

    @Test
    void testSaveTag_emptyTagName() {
        TagRegisterDto tagRegisterDto = new TagRegisterDto();  // tagName 비어있는 경우
        tagRegisterDto.setTagName("");

        TagNotFoundException exception = assertThrows(TagNotFoundException.class, () -> tagService.save(tagRegisterDto));
        assertEquals("tag name is empty", exception.getMessage());
    }

    @Test
    void testFindTagById_tagNotFound() {
        when(tagRepository.existsByTagId(999L)).thenReturn(false);  // 존재하지 않는 tagId

        TagNotFoundException exception = assertThrows(TagNotFoundException.class, () -> tagService.findTagById(999L));
        assertEquals("tag not found", exception.getMessage());
    }

    @Test
    void testSaveTag_tagNameAlreadyExists() {
        TagRegisterDto tagRegisterDto = new TagRegisterDto();
        tagRegisterDto.setTagName("Fiction");

        when(tagRepository.existsByTagName("Fiction")).thenReturn(true);  // 이미 존재하는 태그명

        TagNameAlreadyException exception = assertThrows(TagNameAlreadyException.class, () -> tagService.save(tagRegisterDto));
        assertEquals("이미 존재하는 태그입니다.", exception.getMessage());
    }

    @Test
    void testFindAll() {
        // given
        List<Tag> tags = Arrays.asList(
                new Tag(1L, "Fiction"),
                new Tag(2L, "Science")
        );
        Page<Tag> page = new PageImpl<>(tags, PageRequest.of(0, 2), tags.size());
        when(tagRepository.findAll(PageRequest.of(0, 2))).thenReturn(page);

        // when
        Page<TagResponseDto> result = tagService.findAll(PageRequest.of(0, 2));

        // then
        assertEquals(2, result.getTotalElements());
        assertEquals("Fiction", result.getContent().get(0).getTagName());
        assertEquals("Science", result.getContent().get(1).getTagName());
    }

    @Test
    void testFindAll_emptyPage() {
        // given
        Page<Tag> page = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 2), 0);
        when(tagRepository.findAll(PageRequest.of(0, 2))).thenReturn(page);

        // when
        Page<TagResponseDto> result = tagService.findAll(PageRequest.of(0, 2));

        // then
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }



}
