package com.example.MyBookShopApp.data.other;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "faq")
@Data
public class FaqEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0", name = "sort_index")
    private int sortIndex;
    @Column(nullable = false)
    private String question;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;
}
