package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.repositories.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class SearchService {

    private final BookRepository bookRepository;

    public SearchService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> getPageOfSearchResultBooks(String query, int page, int size) {
        Logger.getLogger(SearchService.class.getName()).info("find <<" + query + ">> with page " + page + " and size " + size);
        Pageable nextPage = PageRequest.of(page, size);
        return bookRepository.findBooksByTitleContaining(query, nextPage);
    }
}
