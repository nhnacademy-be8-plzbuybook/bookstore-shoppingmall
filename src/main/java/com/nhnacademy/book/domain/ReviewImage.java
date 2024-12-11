package com.nhnacademy.book.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private Long reviewImageId;

    @ManyToOne
    @JoinColumn(name = "ri_review_id", nullable = false)
    private Review review;

    @OneToOne
    @JoinColumn(name = "ri_image_id", nullable = false)
    private Image image;
}
