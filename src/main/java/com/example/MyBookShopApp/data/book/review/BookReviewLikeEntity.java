package com.example.MyBookShopApp.data.book.review;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_review_like")
@Data
public class BookReviewLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int reviewId;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime time;
    @Column(nullable = false, columnDefinition = "SMALLINT")
    private short value;
}
