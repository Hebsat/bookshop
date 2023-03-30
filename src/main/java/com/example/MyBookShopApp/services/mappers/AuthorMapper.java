package com.example.MyBookShopApp.services.mappers;

import com.example.MyBookShopApp.api.AuthorDto;
import com.example.MyBookShopApp.data.main.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class AuthorMapper {

    private final BookMapper bookMapper;

    public AuthorDto convertAuthorToAuthorDtoLight(Author author) {
        return AuthorDto.builder()
                .slug(author.getSlug())
                .name(author.getName())
                .build();
    }

    public AuthorDto convertAuthorToAuthorDtoFull(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .photo(author.getPhoto())
                .slug(author.getSlug())
                .name(author.getName())
                .description(author.getDescription())
                .bookList(author.getBookList().stream().map(bookMapper::convertBookToBookDtoLight).collect(Collectors.toList()))
                .build();
    }
}
