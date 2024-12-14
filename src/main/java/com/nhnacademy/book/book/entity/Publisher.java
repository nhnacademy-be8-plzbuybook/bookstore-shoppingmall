package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "publisher")
@AllArgsConstructor
@Getter
public class Publisher {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publisherId;

    @Setter
    @Column(nullable = false, length = 50)
    private String publisherName;

    @Setter
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    public Publisher() {
    }

    public Publisher(String publisherName) {
        this.publisherName = publisherName;
    }
}
