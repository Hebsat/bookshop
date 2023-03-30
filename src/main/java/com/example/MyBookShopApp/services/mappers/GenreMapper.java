package com.example.MyBookShopApp.services.mappers;

import com.example.MyBookShopApp.api.GenreDto;
import com.example.MyBookShopApp.data.main.Genre;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class GenreMapper {

    public GenreDto convertGenreToGenreDto(Genre genre) {
        return GenreDto.builder()
                .slug(genre.getSlug())
                .name(genre.getName())
                .embeddedGenreList(genre.getEmbeddedGenreList().stream().map(this::convertGenreToGenreDto).collect(Collectors.toList()))
                .countOfBooksIncludedEmbeddedGenres(genre.getBookListIncludedBooksOfEmbeddedGenres().size())
                .embeddedEmbedded(getEmbeddedEmbedded(genre))
                .build();
    }

    public boolean getEmbeddedEmbedded(Genre genre) {
        return genre.getEmbeddedGenreList().stream().anyMatch(g -> !g.getEmbeddedGenreList().isEmpty());
    }
}
