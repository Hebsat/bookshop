package com.example.MyBookShopApp.data;

import lombok.Data;

@Data
public class ErrorResponseDto {

    private final boolean result = false;
    private String error;
}
