package com.example.MyBookShopApp.data.other;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "documents")
@Data
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int sortIndex;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;
}
