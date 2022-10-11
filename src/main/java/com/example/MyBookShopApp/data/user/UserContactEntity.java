package com.example.MyBookShopApp.data.user;

import com.example.MyBookShopApp.data.enums.ContactType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_contacts")
@Data
public class UserContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int userId;
    private ContactType type;
    @Column(nullable = false, columnDefinition = "SMALLINT")
    private short approved;
    @Column(nullable = false)
    private String code;
    private int codeTrails;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime codeTime;
    @Column(nullable = false)
    private String contact;
}
