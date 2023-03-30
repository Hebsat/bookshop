package com.example.MyBookShopApp.api;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GenreDto {

    private String slug;
    private String name;
    private List<GenreDto> embeddedGenreList;
    private boolean embeddedEmbedded;
    private int countOfBooksIncludedEmbeddedGenres;
}
