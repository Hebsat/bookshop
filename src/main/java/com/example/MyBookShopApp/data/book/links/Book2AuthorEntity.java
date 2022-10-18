package com.example.MyBookShopApp.data.book.links;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "book2author")
@Data
public class Book2AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, name = "book_id")
    private int bookId;
    @Column(nullable = false, name = "author_id")
    private int authorId;
    @Column(name = "sort_index", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int sortIndex;
}
