package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {


    boolean existsByTagName(String name);

    List<Tag> findByTagNameContaining(String tagName);

    boolean existsByTagId(Long tagId);

    Tag findByTagId(Long tagId);

    Tag findTagByTagId(Long tagId);

//    Page<Category> findByCategoryNameContaining(String keyword, Pageable pageable);

}
