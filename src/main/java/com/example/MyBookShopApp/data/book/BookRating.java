package com.example.MyBookShopApp.data.book;

import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.user.UserEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "book_ratings")
@Getter
@Setter
@NoArgsConstructor
public class BookRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;
    @Column(nullable = false)
    private int rating;

    public BookRating(Book book, UserEntity user) {
        this.book = book;
        this.user = user;
    }
}