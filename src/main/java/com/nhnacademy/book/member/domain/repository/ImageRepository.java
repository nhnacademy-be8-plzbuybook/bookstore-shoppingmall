package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
