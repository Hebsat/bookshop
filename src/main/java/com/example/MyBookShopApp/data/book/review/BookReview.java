package com.example.MyBookShopApp.data.book.review;

import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.user.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "book_review")
@Data
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(nullable = false, name = "book_id", referencedColumnName = "id")
    @ManyToOne
    private Book book;
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime time;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;
    @OneToMany(mappedBy = "review")
    private List<BookReviewLike> likes;
}
