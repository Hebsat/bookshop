package com.example.MyBookShopApp.data;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "book_files")
@Data
public class BookFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String hash;
    @Column(nullable = false)
    private int typeId;
    @Column(nullable = false)
    private String path;
}
