package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorService authorService;

    @Autowired
    public AuthorsController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<Author>> authorsMap() {
        return authorService.getAuthorData();
    }

    @ModelAttribute("topBarIdentifier")
    public String topBarIdentifier() {
        return "authors";
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "bookshop.names.titles.authors";
    }

    @GetMapping
    public String authorsPage() {
        return "authors/index";
    }

    @GetMapping("/{slug}")
    public String getAuthor(@PathVariable String slug, Model model) {
        Logger.getLogger(AuthorsController.class.getName()).info("request id: " + slug);
        model.addAttribute("author", authorService.getAuthor(Integer.parseInt(slug)));
        return "authors/slug";
    }
}
