package com.example.MyBookShopApp.data.book.links;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "book2genre")
@Data
public class Book2GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "book_id",nullable = false)
    private int bookId;
    @Column(name = "genre_id",nullable = false)
    private int genreId;
}
