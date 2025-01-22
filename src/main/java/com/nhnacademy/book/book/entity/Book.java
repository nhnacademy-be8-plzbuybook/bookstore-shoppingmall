package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
@RequiredArgsConstructor
@Getter
@Setter
@Builder
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

    public Book(Long bookId, Publisher publisher, String bookTitle, String bookIndex,
                String bookDescription, LocalDate bookPubDate, BigDecimal bookPriceStandard,
                String bookIsbn13) {
        this.bookId = bookId;
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


    //책을 삭제할때는 판매책도 삭제되게
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

    public Book(long l, String testBook) {
        this.bookId = l;
        this.bookTitle = testBook;
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
