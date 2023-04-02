package com.example.MyBookShopApp.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@Getter
@JsonInclude(NON_NULL)
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
