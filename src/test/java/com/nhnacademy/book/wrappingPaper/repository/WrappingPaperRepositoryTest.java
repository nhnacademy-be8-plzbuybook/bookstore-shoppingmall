package com.nhnacademy.book.wrappingPaper.repository;

import com.nhnacademy.book.config.JpaRepositoryConfig;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(JpaRepositoryConfig.class)
class WrappingPaperRepositoryTest {
    @Autowired
    private WrappingPaperRepository wrappingPaperRepository;
    private WrappingPaper existingWrappingPaper;

    @BeforeEach
    void setup() {
        WrappingPaper wrappingPaper = new WrappingPaper("test", new BigDecimal("1000"), 100L, LocalDateTime.now(), "/image/path");
        existingWrappingPaper = wrappingPaperRepository.save(wrappingPaper);
    }

    @Test
    void get() {
        Optional<WrappingPaper> optionalWrappingPaper = wrappingPaperRepository.findById(existingWrappingPaper.getId());

        assertTrue(optionalWrappingPaper.isPresent());
        WrappingPaper wrappingPaper = optionalWrappingPaper.get();
        assertEquals("test", wrappingPaper.getName());
        assertEquals(0, wrappingPaper.getPrice().compareTo(new BigDecimal("1000")));
        assertEquals(100L, wrappingPaper.getStock());
        assertEquals("/image/path", wrappingPaper.getImagePath());
    }

    @Test
    void save() {
        WrappingPaper wrappingPaper = new WrappingPaper("test2", new BigDecimal("1000"), 100L, LocalDateTime.now(), "/image/path");
        WrappingPaper savedWrappingPaper = wrappingPaperRepository.save(wrappingPaper);

        Optional<WrappingPaper> optionalWrappingPaper = wrappingPaperRepository.findById(savedWrappingPaper.getId());

        assertTrue(optionalWrappingPaper.isPresent());
        assertEquals(wrappingPaper.getName(), savedWrappingPaper.getName());
        assertEquals(0, wrappingPaper.getPrice().compareTo(wrappingPaper.getPrice()));
        assertEquals(wrappingPaper.getStock(), wrappingPaper.getStock());
        assertEquals(wrappingPaper.getImagePath(), wrappingPaper.getImagePath());
    }

    @Test
    void save_unique_constraint_violation() {
        WrappingPaper wrappingPaper = new WrappingPaper("test", new BigDecimal("1000"), 100L, LocalDateTime.now(), "/image/path");
        assertThrows(DataIntegrityViolationException.class, () -> wrappingPaperRepository.save(wrappingPaper));
    }

    @Test
    void getAll() {
        WrappingPaper wrappingPaper = new WrappingPaper("test2", new BigDecimal("1000"), 100L, LocalDateTime.now(), "/image/path");
        wrappingPaperRepository.save(wrappingPaper);

        List<WrappingPaper> wrappingPaperList = wrappingPaperRepository.findAll();
        assertEquals(2, wrappingPaperList.size());
    }

    @Test
    void update() {
        long id = existingWrappingPaper.getId();
        Optional<WrappingPaper> optionalWrappingPaper = wrappingPaperRepository.findById(id);

        assertTrue(optionalWrappingPaper.isPresent());
        existingWrappingPaper = optionalWrappingPaper.get();

        existingWrappingPaper.update("update", new BigDecimal("2000"), 12L, "update/img");


        optionalWrappingPaper = wrappingPaperRepository.findById(id);
        assertTrue(optionalWrappingPaper.isPresent());
        WrappingPaper updatedWrappingPaper = optionalWrappingPaper.get();

        assertEquals("update", updatedWrappingPaper.getName());
    }

    @Test
    void delete() {
        long id = existingWrappingPaper.getId();
        wrappingPaperRepository.deleteById(id);

        Optional<WrappingPaper> optionalWrappingPaper = wrappingPaperRepository.findById(id);
        assertTrue(optionalWrappingPaper.isEmpty());
    }

}