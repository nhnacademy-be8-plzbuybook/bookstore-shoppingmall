package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.dto.response.BookTagResponseDto;
import com.nhnacademy.book.book.entity.BookTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookTagRepository extends JpaRepository<BookTag, Long> {

//    @Query("SELECT bt.book.bookId FROM BookTag bt WHERE bt.tag.tagId = :tagId")
    List<Long> findBook_BookIdsByTag_TagId(Long tagId);


    List<Long> findBookTagIdsByTag_TagId(Long tagId);

    @Query("SELECT new com.nhnacademy.book.book.dto.response.BookTagResponseDto(" +
            "bt.bookTagId, bt.tag.tagId, bt.tag.tagName, bt.book.bookId, bt.book.bookTitle) " +
            "FROM BookTag bt " +
            "JOIN bt.tag t " +
            "JOIN bt.book b " +
            "WHERE bt.tag.tagId = :tagId")
    List<BookTagResponseDto> findBookTagResponseDtosByTagId(@Param("tagId") Long tagId);

    List<BookTag> findBookTagByBook_BookId(Long bookId);


    void deleteByBook_BookIdAndTag_TagId(Long bookId, Long tagId);

}
