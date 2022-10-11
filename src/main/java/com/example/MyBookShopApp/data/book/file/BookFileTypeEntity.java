package com.example.MyBookShopApp.data.book.file;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "book_file_types")
@Data
public class BookFileTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
}
