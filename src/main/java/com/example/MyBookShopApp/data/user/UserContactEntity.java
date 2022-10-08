package com.example.MyBookShopApp.data.user;

import com.example.MyBookShopApp.data.enums.ContactType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_contacts")
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public short getApproved() {
        return approved;
    }

    public void setApproved(short approved) {
        this.approved = approved;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCodeTrails() {
        return codeTrails;
    }

    public void setCodeTrails(int codeTrails) {
        this.codeTrails = codeTrails;
    }

    public LocalDateTime getCodeTime() {
        return codeTime;
    }

    public void setCodeTime(LocalDateTime codeTime) {
        this.codeTime = codeTime;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
