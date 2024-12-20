package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByImagePath(String path);
}
