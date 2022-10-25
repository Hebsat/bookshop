package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.repositories.AuthorRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Value("${bookshop.default.offset}")
    private int defaultOffset;
    @Value("${bookshop.default.limit}")
    private int defaultLimit;

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Map<String, List<Author>> getAuthorData() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream().collect(Collectors.groupingBy(a -> a.getName().substring(0, 1)));
    }

    public Page<Book> getPageOfBooksByAuthor(Author author, int page, int size) {
        Pageable nextPage = PageRequest.of(page, size);
        return bookRepository.findBooksByAuthorListContains(author, nextPage);
    }

    public Page<Book> getPageOfBooksByAuthor(Author author) {
        return getPageOfBooksByAuthor(author, defaultOffset, defaultLimit);
    }

    public Page<Book> getPageOfBooksByAuthor(String slug, int page, int size) {
        return getPageOfBooksByAuthor(getAuthorBySlug(slug), page, size);
    }

    public Author getAuthorBySlug(String slug) {
        Logger.getLogger(AuthorService.class.getName()).info("getAuthorBySlug with slug: " + slug);
        return authorRepository.findAuthorBySlug(slug);
    }

    public Author getAuthorById(int id) {
        Logger.getLogger(AuthorService.class.getName()).info("getAuthorById with id: " + id);
        return authorRepository.findById(id).get();
    }
}
