package com.example.MyBookShopApp.data.genre;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "genres")
@Data
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int parentId;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private String name;
}
