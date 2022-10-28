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
    @Column(nullable = false, name = "user_id")
    private int userId;
    private ContactType type;
    @Column(nullable = false, columnDefinition = "SMALLINT")
    private short approved;
    @Column(nullable = false)
    private String code;
    @Column(name = "code_trails")
    private int codeTrails;
    @Column(columnDefinition = "TIMESTAMP", name = "code_time")
    private LocalDateTime codeTime;
    @Column(nullable = false)
    private String contact;
}
