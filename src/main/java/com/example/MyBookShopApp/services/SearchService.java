package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.api.BooksListDto;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.services.mappers.BookMapper;
import com.example.MyBookShopApp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BooksListDto getPageOfSearchResultBooks(String query, int page, int size) {
        Logger.getLogger(SearchService.class.getName()).info("find <<" + query + ">> with page " + page + " and size " + size);
        Pageable nextPage = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findBooksByTitleContaining(query, nextPage);
        return BooksListDto.builder()
                .totalPages(books.getTotalPages())
                .count((int) books.getTotalElements())
                .books(books.getContent().stream().map(bookMapper::convertBookToBookDtoLight).collect(Collectors.toList()))
                .build();
    }
}
