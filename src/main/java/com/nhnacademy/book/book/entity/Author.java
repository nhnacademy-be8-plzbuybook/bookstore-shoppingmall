package com.nhnacademy.book.book.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "author")
@RequiredArgsConstructor
@AllArgsConstructor
@Document(indexName = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    @Column(name= "author_name")
    private String authorName;

    @OneToMany(mappedBy = "author")
    private List<BookAuthor> bookAuthors = new ArrayList<>();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;  // Soft Delete 필드

    public Author(String authorName) {
        this.authorName = authorName;
    }
}
