package com.example.MyBookShopApp.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookFileDto {

    private String hash;
    private String fileType;
    private String description;
}
