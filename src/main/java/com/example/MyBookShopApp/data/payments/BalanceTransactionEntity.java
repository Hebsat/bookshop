package com.example.MyBookShopApp.data.payments;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance_transactions")
@Data
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
}
