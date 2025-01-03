package com.nhnacademy.book.book.elastic.document;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "likes_4")

public class LikesDocument {

    @org.springframework.data.annotation.Id
    private Long likesId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "selling_book_id", nullable = false)
    private SellingBook sellingBook;
}

