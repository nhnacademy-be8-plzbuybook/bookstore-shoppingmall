package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByAuthorName(String authorName);

    @Transactional
    @Modifying
    @Query("UPDATE Author a SET a.deletedAt = CURRENT_TIMESTAMP WHERE a.authorId = :authorId")
    void softDeleteById(Long authorId);

    Page<Author> findByAuthorNameContaining(String authorName, Pageable pageable);

}
