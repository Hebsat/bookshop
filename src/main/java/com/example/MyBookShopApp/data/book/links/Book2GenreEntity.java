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
    @Column(nullable = false)
    private int bookId;
    @Column(nullable = false)
    private int genreId;
}
