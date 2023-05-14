package com.example.MyBookShopApp.api;

import lombok.Data;

@Data
public class RateBookRequest {

    private String bookId;
    private Short value;
}
