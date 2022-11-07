package com.example.MyBookShopApp.errors;

public class EmptySearchQueryException extends Exception {

    public EmptySearchQueryException(String message) {
        super(message);
    }
}
