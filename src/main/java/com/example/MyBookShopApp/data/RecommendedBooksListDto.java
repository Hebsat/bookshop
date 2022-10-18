package com.example.MyBookShopApp.data;

import lombok.Data;

import java.util.List;

@Data
public class RecommendedBooksListDto {

    private int count;
    private List<Book> bookList;

    public RecommendedBooksListDto(List<Book> bookList) {
        this.bookList = bookList;
        this.count = bookList.size();
    }
}
