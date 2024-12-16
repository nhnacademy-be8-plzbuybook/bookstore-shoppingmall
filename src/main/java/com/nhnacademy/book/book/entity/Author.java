package com.nhnacademy.book.book.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "author")
@RequiredArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    @Column(name= "author_name")
    private String authorName;

    @OneToMany(mappedBy = "author")
    private List<BookAuthor> bookAuthors = new ArrayList<>();


    @Override
    public String toString() {
        return "authorId: " + authorId + ", " +"authorName: "+authorName;
    }
}
