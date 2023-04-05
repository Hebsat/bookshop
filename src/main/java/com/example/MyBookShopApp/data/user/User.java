package com.example.MyBookShopApp.data.user;

import com.example.MyBookShopApp.data.book.BookRating;
import com.example.MyBookShopApp.data.book.review.BookReview;
import com.example.MyBookShopApp.data.book.review.BookReviewLike;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String hash;
    @Column(nullable = false, columnDefinition = "TIMESTAMP", name = "reg_time")
    private LocalDateTime regTime;
    @Column(nullable = false)
    private int balance;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "user")
    private List<BookRating> ratings;
    @OneToMany(mappedBy = "user")
    private List<BookReview> bookReviews;
    @OneToMany(mappedBy = "user")
    private List<BookReviewLike> reviewLikes;
}
