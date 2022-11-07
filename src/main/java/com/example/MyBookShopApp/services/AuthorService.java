package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.main.Author;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.errors.WrongEntityException;
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

    @Value("${bookshop.default.page}")
    private int defaultPage;
    @Value("${bookshop.default.size}")
    private int defaultSize;

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Map<String, List<Author>> getAuthorData() {
        Logger.getLogger(AuthorService.class.getName()).info(
                "getAuthorData");
        List<Author> authors = authorRepository.findAll();
        return authors.stream().collect(Collectors.groupingBy(a -> a.getName().substring(0, 1)));
    }

    public Page<Book> getPageOfBooksByAuthor(Author author, int page, int size) {
        Logger.getLogger(AuthorService.class.getName()).info(
                "getPageOfBooksByAuthor: " + author.getName() + " with page " + page + " and size " + size);
        Pageable nextPage = PageRequest.of(page, size);
        return bookRepository.findBooksByAuthorListContains(author, nextPage);
    }

    public Page<Book> getPageOfBooksByAuthor(Author author) {
        return getPageOfBooksByAuthor(author, defaultPage, defaultSize);
    }

    public Author getAuthorById(int id) throws BookshopWrongParameterException {
        return authorRepository.findById(id)
                .orElseThrow(() -> new BookshopWrongParameterException("Author with the specified id " + id + " does not exist"));
    }

    public Author getAuthorBySlug(String slug) throws WrongEntityException {
        return authorRepository.findAuthorBySlug(slug)
                .orElseThrow(() -> new WrongEntityException("Автора с идентификатором " + slug + " не существует"));
    }
}
