package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Map<String, List<Author>> getAuthorData() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream().collect(Collectors.groupingBy(a -> a.getName().substring(0, 1)));
    }

    public Author getAuthorBySlug(String slug) {
        Logger.getLogger(AuthorService.class.getName()).info("getAuthorBySlug with slug: " + slug);
        return authorRepository.findBySlug(slug);
    }

    public Author getAuthor(int id) {
        Logger.getLogger(AuthorService.class.getName()).info("getAuthorById with id: " + id);
        return authorRepository.findById(id).get();
    }
}
