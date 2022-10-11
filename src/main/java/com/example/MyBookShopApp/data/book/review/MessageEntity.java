package com.example.MyBookShopApp.data.book.review;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime time;
    private int userId;
    private String email;
    private String name;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;
}
