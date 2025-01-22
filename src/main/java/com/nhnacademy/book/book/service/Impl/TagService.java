package com.nhnacademy.book.book.service.Impl;


import com.nhnacademy.book.book.dto.request.TagRegisterDto;
import com.nhnacademy.book.book.dto.response.TagResponseDto;
import com.nhnacademy.book.book.entity.Tag;
import com.nhnacademy.book.book.exception.TagNameAlreadyException;
import com.nhnacademy.book.book.exception.TagNotFoundException;
import com.nhnacademy.book.book.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    private static final String TAG_NOT_FOUND_MESSAGE = "tag not found";


    public TagResponseDto save(TagRegisterDto tagRegisterDto) {

        if(tagRegisterDto.getTagName().isEmpty()){
            throw new TagNotFoundException("tag name is empty");
        }
        if (tagRepository.existsByTagName(tagRegisterDto.getTagName())) {
            throw new TagNameAlreadyException("이미 존재하는 태그입니다.");
        }
        Tag tag = new Tag();
        tag.setTagName(tagRegisterDto.getTagName());

        Tag newTag = tagRepository.save(tag);

        return new TagResponseDto(newTag.getTagId(),newTag.getTagName());

    }

    public Tag findTagById(Long tagId) {
        if(tagRepository.existsByTagId(tagId)){
            return tagRepository.findByTagId(tagId);
        } else throw new TagNotFoundException(TAG_NOT_FOUND_MESSAGE);
    }


    public Page<TagResponseDto> searchTagsByKeyword(String keyword, Pageable pageable) {
        Page<Tag> tagsPage = tagRepository.findByTagNameContaining(keyword, pageable);
        return tagsPage.map(tag -> new TagResponseDto(tag.getTagId(), tag.getTagName()));
    }

    public Page<TagResponseDto> findAll(Pageable pageable) {
        Page<Tag> tagsPage = tagRepository.findAll(pageable);
        return tagsPage.map(tag -> new TagResponseDto(tag.getTagId(), tag.getTagName()));
    }


    public void deleteTagById(Long tagId) {
        if(tagRepository.existsByTagId(tagId)){
            tagRepository.deleteById(tagId);
        } else {
            throw new TagNotFoundException(TAG_NOT_FOUND_MESSAGE);
        }
    }

    public String findTagNameByTagId(Long tagId) {
        if(!tagRepository.existsByTagId(tagId)){
            throw new TagNotFoundException(TAG_NOT_FOUND_MESSAGE);
        }

        Tag tag = tagRepository.findTagByTagId(tagId);
        return tag.getTagName();
    }




}
