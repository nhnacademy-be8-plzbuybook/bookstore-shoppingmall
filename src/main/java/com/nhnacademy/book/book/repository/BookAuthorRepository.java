package com.nhnacademy.book.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
}
