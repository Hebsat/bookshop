package com.example.MyBookShopApp.data.book.review;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_review")
@Data
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int bookId;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime time;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;
}
