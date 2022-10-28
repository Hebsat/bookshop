package com.example.MyBookShopApp.data.book.links;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book2user")
@Data
public class Book2UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime time;
    @Column(nullable = false, name = "type_id")
    private int typeId;
    @Column(nullable = false, name = "book_id")
    private int bookId;
    @Column(nullable = false, name = "user_id")
    private int userId;
}
