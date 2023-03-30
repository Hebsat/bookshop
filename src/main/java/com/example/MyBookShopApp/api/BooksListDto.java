package com.example.MyBookShopApp.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BooksListDto {

    @ApiModelProperty(position = 1)
    private String message;
    @ApiModelProperty(position = 2)
    private int count;
    @ApiModelProperty(position = 4)
    private int totalPages;
    @ApiModelProperty(position = 3)
    private List<BookDto> books;
}
