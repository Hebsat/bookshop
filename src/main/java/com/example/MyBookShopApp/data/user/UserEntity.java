package com.example.MyBookShopApp.data.user;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String hash;
    @Column(nullable = false, columnDefinition = "TIMESTAMP", name = "reg_time")
    private LocalDateTime regTime;
    @Column(nullable = false)
    private int balance;
    @Column(nullable = false)
    private String name;
}
