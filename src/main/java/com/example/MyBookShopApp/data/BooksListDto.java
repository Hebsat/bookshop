package com.example.MyBookShopApp.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BooksListDto {

    @ApiModelProperty(position = 1)
    private int count;
    @ApiModelProperty(position = 2)
    private List<Book> books;

    public BooksListDto(List<Book> bookList) {
        this.books = bookList;
        this.count = bookList.size();
    }
}
