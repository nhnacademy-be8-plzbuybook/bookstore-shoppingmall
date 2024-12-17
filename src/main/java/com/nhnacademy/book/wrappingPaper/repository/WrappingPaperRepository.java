package com.nhnacademy.book.wrappingPaper.repository;

import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WrappingPaperRepository extends JpaRepository<WrappingPaper, Long> {
    boolean existsByName(@NotBlank String name);
}

