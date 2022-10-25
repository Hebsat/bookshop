package com.example.MyBookShopApp.data;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "books_popularity")
@Data
public class BookPopularity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int countPurchases;
    private int countInCart;
    private int countPostponed;
}
