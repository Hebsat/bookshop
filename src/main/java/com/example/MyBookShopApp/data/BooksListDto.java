package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.main.Book;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class BooksListDto {

    @ApiModelProperty(position = 1)
    private String message = "successful request";
    @ApiModelProperty(position = 2)
    private int count;
    @ApiModelProperty(position = 3)
    private List<Book> books;

    public BooksListDto(List<Book> bookList) {
        this.books = bookList;
        this.count = bookList.size();
    }

    public BooksListDto(int count, List<Book> bookList) {
        this.books = bookList;
        this.count = count;
    }

    public BooksListDto(String message) {
        this.message = message;
        this.books = new ArrayList<>();
    }
}
