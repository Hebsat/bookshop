package com.example.MyBookShopApp.api;

import lombok.Data;

import java.util.List;

@Data
public class ChangeBookStatusRequest {

    private List<String> booksIds;
    private String status;
}
