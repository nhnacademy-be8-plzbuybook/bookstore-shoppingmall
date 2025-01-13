package com.nhnacademy.book.book.entity;

import com.nhnacademy.book.book.elastic.repository.BookSearchRepository;
import com.nhnacademy.book.converter.PasswordConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Book {


    public Book(Publisher publisher, String bookTitle, String bookIndex, String bookDescription,
                LocalDate bookPubDate, BigDecimal bookPriceStandard, String bookIsbn13) {
        this.publisher = publisher;
        this.bookTitle = bookTitle;
        this.bookIndex = bookIndex;
        this.bookDescription = bookDescription;
        this.bookPubDate = bookPubDate;
        this.bookPriceStandard = bookPriceStandard;
        this.bookIsbn13 = bookIsbn13;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @Column(nullable = false, length = 255)
    private String bookTitle;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String bookIndex;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String bookDescription;

    @Column(nullable = false)
    private LocalDate bookPubDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal bookPriceStandard;

    @Column(nullable = false, length = 40, unique = true)
    private String bookIsbn13;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellingBook> sellingBooks = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookCategory> bookCategories = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookAuthor> bookAuthors = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookTag> bookTags = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookImage> bookImages = new ArrayList<>();

    // 이미지 추가 메서드
    public void addImage(String imageUrl) {
        BookImage bookImage = new BookImage(this, imageUrl);
        this.bookImages.add(bookImage);
    }
    public Book(String bookTitle, String bookIndex,
                String bookDescription, LocalDate bookPubDate,
                BigDecimal sellingBookPrice, String bookIsbn13,
                Publisher publisher, String imageUrl) {
    }

    public Book(String bookTitle, String bookIsbn13,
                Object o, Object o1,
                LocalDate bookPubDate,
                BigDecimal sellingBookPrice,
                String bookIsbn131, String publisher, String imageUrl) {
    }


    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookTitle='" + bookTitle + '\'' +
                '}';
    }
    // addCategory 메서드 추가
    public void addCategory(Category category) {
        if (bookCategories.stream().noneMatch(bc -> bc.getCategory().equals(category))) {
            BookCategory bookCategory = new BookCategory(this, category);
            this.bookCategories.add(bookCategory);
            category.getBookCategories().add(bookCategory);
        }
    }

    public void addAuthor(Author author) {
        if (bookAuthors.stream().noneMatch(ba -> ba.getAuthor().equals(author))) {
            BookAuthor bookAuthor = new BookAuthor(this, author);
            this.bookAuthors.add(bookAuthor);
            author.getBookAuthors().add(bookAuthor);
        }
    }



}
