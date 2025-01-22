package com.nhnacademy.book.book.elastic.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Document(indexName = "selling_book_4")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SellingBookDocument {

    @org.springframework.data.annotation.Id
    private Long sellingBookId;

    @Field(type = FieldType.Long)
    private Long bookId; // Book의 ID를 저장

    @Field(type = FieldType.Double)
    private BigDecimal sellingBookPrice;

    @Field(type = FieldType.Boolean)
    private Boolean sellingBookPackageable;

    @Field(type = FieldType.Integer)
    private Integer sellingBookStock;

    @Field(type = FieldType.Keyword)
    private String sellingBookStatus; // ENUM 값을 문자열로 저장

    @Field(type = FieldType.Boolean)
    private Boolean used;

    @Field(type = FieldType.Long)
    private Long sellingBookViewCount;

    @Override
    public String toString() {
        return "SellingBookDocument{" +
                "sellingBookId=" + sellingBookId +
                ", bookId=" + bookId +
                ", sellingBookPrice=" + sellingBookPrice +
                ", sellingBookPackageable=" + sellingBookPackageable +
                ", sellingBookStock=" + sellingBookStock +
                ", sellingBookStatus='" + sellingBookStatus + '\'' +
                ", used=" + used +
                ", sellingBookViewCount=" + sellingBookViewCount +
                '}';
    }
}
