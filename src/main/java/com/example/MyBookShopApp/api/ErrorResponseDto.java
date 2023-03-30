package com.example.MyBookShopApp.api;

import lombok.Data;

@Data
public class ErrorResponseDto {

    private final boolean result = false;
    private String error;
}
