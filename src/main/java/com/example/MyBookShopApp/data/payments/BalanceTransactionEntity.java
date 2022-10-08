package com.example.MyBookShopApp.data.payments;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance_transactions")
public class BalanceTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime time;
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int value;
    @Column(nullable = false)
    private int bookId;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
