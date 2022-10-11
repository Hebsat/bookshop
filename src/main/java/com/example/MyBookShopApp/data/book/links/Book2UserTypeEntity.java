package com.example.MyBookShopApp.data.book.links;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "book2user_type")
@Data
public class Book2UserTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
}
